CREATE VIRTUAL TABLE rtree_${t}_${c} USING rtree(id, minx, maxx, miny, maxy);

INSERT OR REPLACE INTO rtree_${t}_${c}
  SELECT ${i}, ST_MinX(${c}), ST_MaxX(${c}), ST_MinY(${c}), ST_MaxY(${c}) FROM ${t}
  WHERE NOT ST_IsEmpty(${c});

-- Conditions: Insertion of non-empty geometry
--   Actions   : Insert record into rtree 
CREATE TRIGGER rtree_${t}_${c}_insert AFTER INSERT ON ${t}
  WHEN (new.${c} NOT NULL AND NOT ST_IsEmpty(NEW.${c}))
BEGIN
  INSERT OR REPLACE INTO rtree_${t}_${c} VALUES (
    NEW.${i},
    ST_MinX(NEW.${c}), ST_MaxX(NEW.${c}),
    ST_MinY(NEW.${c}), ST_MaxY(NEW.${c})
  );
END;

-- Conditions: Update of geometry column to non-empty geometry
--               No row ID change
--   Actions   : Update record in rtree 
CREATE TRIGGER rtree_${t}_${c}_update1 AFTER UPDATE OF ${c} ON ${t}
  WHEN OLD.${i} = NEW.${i} AND
       (NEW.${c} NOTNULL AND NOT ST_IsEmpty(NEW.${c}))
BEGIN
  INSERT OR REPLACE INTO rtree_${t}_${c} VALUES (
    NEW.${i},
    ST_MinX(NEW.${c}), ST_MaxX(NEW.${c}),
    ST_MinY(NEW.${c}), ST_MaxY(NEW.${c})
  );
END;

-- Conditions: Update of geometry column to empty geometry
--               No row ID change
--   Actions   : Remove record from rtree 
CREATE TRIGGER rtree_${t}_${c}_update2 AFTER UPDATE OF ${c} ON ${t}
  WHEN OLD.${i} = NEW.${i} AND
       (NEW.${c} ISNULL OR ST_IsEmpty(NEW.${c}))
BEGIN
  DELETE FROM rtree_${t}_${c} WHERE id = OLD.${i};
END;

-- Conditions: Update of any column
--               Row ID change
--              Non-empty geometry
--   Actions   : Remove record from rtree for old ${i}
--               Insert record into rtree for new ${i}
CREATE TRIGGER rtree_${t}_${c}_update3 AFTER UPDATE OF ${c} ON ${t}
  WHEN OLD.${i} != NEW.${i} AND
       (NEW.${c} NOTNULL AND NOT ST_IsEmpty(NEW.${c}))
BEGIN
  DELETE FROM rtree_${t}_${c} WHERE id = OLD.${i};
  INSERT OR REPLACE INTO rtree_${t}_${c} VALUES (
    NEW.${i},
    ST_MinX(NEW.${c}), ST_MaxX(NEW.${c}),
    ST_MinY(NEW.${c}), ST_MaxY(NEW.${c})
  );
END;

-- Conditions: Update of any column
--               Row ID change
--               Empty geometry
--   Actions   : Remove record from rtree for old and new ${i} 
CREATE TRIGGER rtree_${t}_${c}_update4 AFTER UPDATE ON ${t}
  WHEN OLD.${i} != NEW.${i} AND
       (NEW.${c} ISNULL OR ST_IsEmpty(NEW.${c}))
BEGIN
  DELETE FROM rtree_${t}_${c} WHERE id IN (OLD.${i}, NEW.${i});
END;

-- Conditions: Row deleted
--   Actions   : Remove record from rtree for old ${i} 
CREATE TRIGGER rtree_${t}_${c}_delete AFTER DELETE ON ${t}
  WHEN old.${c} NOT NULL
BEGIN
  DELETE FROM rtree_${t}_${c} WHERE id = OLD.${i};
END;

-- Register the spatial index extension for this table/column
INSERT INTO gpkg_extensions(table_name, column_name, extension_name, definition, scope) 
  VALUES('${t}', '${c}', 'gpkg_rtree_index', 'GeoPackage 1.0 Specification Annex L', 'write-only');