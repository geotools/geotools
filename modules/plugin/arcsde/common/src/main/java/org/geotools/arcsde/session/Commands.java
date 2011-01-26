package org.geotools.arcsde.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.logging.Loggers;
import org.geotools.arcsde.versioning.ArcSdeVersionHandler;

import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeDBMSInfo;
import com.esri.sde.sdk.client.SeDelete;
import com.esri.sde.sdk.client.SeError;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeFilter;
import com.esri.sde.sdk.client.SeInsert;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeObjectId;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeQueryInfo;
import com.esri.sde.sdk.client.SeRasterColumn;
import com.esri.sde.sdk.client.SeRegistration;
import com.esri.sde.sdk.client.SeRelease;
import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeSqlConstruct;
import com.esri.sde.sdk.client.SeState;
import com.esri.sde.sdk.client.SeStreamOp;
import com.esri.sde.sdk.client.SeTable;
import com.esri.sde.sdk.client.SeUpdate;
import com.esri.sde.sdk.client.SeVersion;

public class Commands {

    private static final Logger LOGGER = Loggers.getLogger("org.geotools.arcsde.session");

    /**
     * Creates either a direct child state of parentStateId, or a sibling being an exact copy of
     * parentStatId if either the state can't be closed because its in use or parentStateId does not
     * belong to the current user.
     */
    public static final class CreateVersionStateCommand extends Command<SeState> {

        private final long parentStateId;

        public CreateVersionStateCommand(final long parentStateId) {
            this.parentStateId = parentStateId;
        }

        @Override
        public SeState execute(ISession session, SeConnection connection) throws SeException,
                IOException {
            SeState parentState = new SeState(connection, new SeObjectId(parentStateId));

            SeState realParent = null;

            boolean mergeParentToRealParent = false;

            if (parentState.isOpen()) {
                // only closed states can have child states
                try {
                    parentState.close();
                    realParent = parentState;
                } catch (SeException e) {
                    final int errorCode = e.getSeError().getSdeError();
                    if (SeError.SE_STATE_INUSE == errorCode
                            || SeError.SE_NO_PERMISSIONS == errorCode) {
                        // it's not our state or somebody's editing it so we
                        // need to clone the parent,
                        // starting from the parent of the parent
                        realParent = new SeState(connection, parentState.getParentId());
                        mergeParentToRealParent = true;
                    } else {
                        throw e;
                    }
                }
            } else {
                realParent = parentState;
            }

            // create the new state
            SeState newState = new SeState(connection);
            newState.create(realParent.getId());

            if (mergeParentToRealParent) {
                // a sibling of parentStateId was created instead of a
                // child, we need to merge the changes
                // in parentStateId to the new state so they refer to the
                // same content.
                // SE_state_merge applies changes to a parent state to
                // create a new merged state.
                // The new state is the child of the parent state with the
                // changes of the second state.
                // Both input states must have the same parent state.
                // When a row has been changed in both parent and second
                // states, the row from the changes state is used.
                // The parent and changes states must be open or owned by
                // the current user unless the current user is the ArcSDE
                // DBA.
                newState.merge(realParent.getId(), parentState.getId());
            }

            return newState;
        }

    }

    /**
     * Command to create, prepare, and execute a query.
     */
    public static final class CreateAndExecuteQueryCommand extends Command<SeQuery> {

        private final String[] propertyNames;

        private final SeSqlConstruct sql;

        public CreateAndExecuteQueryCommand(final String[] propertyNames, final SeSqlConstruct sql) {
            this.propertyNames = propertyNames;
            this.sql = sql;
        }

        @Override
        public SeQuery execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            SeQuery query = new SeQuery(connection, propertyNames, sql);
            query.prepareQuery();
            query.execute();
            return query;
        }

    }

    public static final class CreateSeStateCommand extends Command<SeState> {

        private final SeObjectId stateId;

        public CreateSeStateCommand(final SeObjectId stateId) {
            this.stateId = stateId;
        }

        @Override
        public SeState execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            return new SeState(connection, stateId);
        }
    }

    /**
     * A command to close an {@link SeStreamOp stream} (ie, {@link SeDelete}, {@link SeInsert},
     * {@link SeQuery}, {@link SeUpdate}).
     */
    public static final class CloseStreamCommand extends Command<Object> {

        private final SeStreamOp stream;

        public CloseStreamCommand(final SeStreamOp stream) {
            this.stream = stream;
        }

        @Override
        public Void execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            stream.close();
            return null;
        }
    }

    public static final class CloseStateCommand extends Command<Object> {

        private final SeState state;

        public CloseStateCommand(final SeState state) {
            this.state = state;
        }

        @Override
        public Void execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            state.close();
            return null;
        }
    }

    public static final class DescribeTableCommand extends Command<SeColumnDefinition[]> {

        private final SeTable table;

        public DescribeTableCommand(SeTable table) {
            this.table = table;
        }

        @Override
        public SeColumnDefinition[] execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            return table.describe();
        }
    }

    public static class CreateSeTableCommand extends Command<SeTable> {

        private final String qualifiedName;

        public CreateSeTableCommand(final String qualifiedName) {
            this.qualifiedName = qualifiedName;
        }

        @Override
        public SeTable execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            return new SeTable(connection, qualifiedName);
        }
    }

    /**
     * Command to check a connection is alive
     */
    public static final Command<Void> TEST_SERVER = new Command<Void>() {
        @Override
        public Void execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            connection.testServer(Session.TEST_SERVER_ROUNDTRIP_INTERVAL_SECONDS);

            return null;
        }
    };

    /**
     * Command to fetch the default version
     */
    public static final Command<SeVersion> GET_DEFAULT_VERSION = new GetVersionCommand(
            SeVersion.SE_QUALIFIED_DEFAULT_VERSION_NAME);

    /**
     * Command to fetch a version.
     * 
     * @author Gabriel Roldan
     */
    public static final class GetVersionCommand extends Command<SeVersion> {

        private String versionName;

        public GetVersionCommand(final String versionName) {
            this.versionName = versionName;
        }

        @Override
        public SeVersion execute(ISession session, SeConnection connection) throws SeException,
                IOException {

            final SeVersion version;
            try {
                version = new SeVersion(connection, versionName);
            } catch (SeException cause) {

                if (cause.getSeError().getSdeError() == -126) {
                    ArrayList<String> available = new ArrayList<String>();
                    try {
                        SeVersion[] versionList = connection.getVersionList(null);
                        for (SeVersion v : versionList) {
                            available.add(v.getName());
                        }
                        throw new ArcSdeException("Specified ArcSDE version does not exist: "
                                + versionName + ". Available versions are: " + available, cause);
                    } catch (SeException ignorable) {
                        // hum... ignore
                        throw new ArcSdeException("Specified ArcSDE version does not exist: "
                                + versionName, cause);
                    }
                } else {
                    throw cause;
                }
            }
            version.getInfo();
            return version;
        }
    }

    public static final class GetTableCommand extends Command<SeTable> {

        private final String tableName;

        public GetTableCommand(final String tableName) {
            this.tableName = tableName;
        }

        @Override
        public SeTable execute(ISession session, SeConnection connection) throws SeException,
                IOException {
            SeTable table = new SeTable(connection, tableName);

            try {
                table.describe();
            } catch (SeException e) {
                throw new IOException("Table does not exist: " + tableName);
            }

            return table;
        }

    }

    public static final class GetLayerCommand extends Command<SeLayer> {

        private final SeTable table;

        public GetLayerCommand(final SeTable table) {
            this.table = table;
        }

        /**
         * @return the SeLayer corresponding to the provided SeTable, or {@code null} if the table
         *         has no shape column
         */
        public SeLayer execute(ISession session, SeConnection connection) throws SeException,
                IOException {
            final String shapeColumn = getShapeColumn(table);
            if (shapeColumn == null) {
                return null;
            }
            final String layerName = table.getQualifiedName();

            SeLayer layer = new SeLayer(connection, layerName, shapeColumn);

            return layer;
        }

        private String getShapeColumn(SeTable table) throws ArcSdeException {
            try {
                for (SeColumnDefinition aDef : table.describe()) {
                    if (aDef.getType() == SeColumnDefinition.TYPE_SHAPE) {
                        return aDef.getName();
                    }
                }
            } catch (SeException e) {
                throw new ArcSdeException("Exception describing table " + table.getName(), e);
            }
            return null;
        }
    }

    public static class FetchRowCommand extends Command<SdeRow> {

        private final SeQuery query;

        private final SdeRow holder;

        public FetchRowCommand(final SeQuery query, final SdeRow holder) {
            this.query = query;
            this.holder = holder;
        }

        @Override
        public SdeRow execute(ISession session, SeConnection connection) throws SeException,
                IOException {
            SeRow row = query.fetch();
            if (row == null) {
                return null;
            } else {
                holder.setRow(row);
            }
            return holder;
        }

    }

    public static final Command<Void> START_TRANSACTION = new Command<Void>() {
        @Override
        public Void execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            connection.setTransactionAutoCommit(0);
            connection.startTransaction();
            return null;
        }
    };

    public static final Command<List<String>> GET_RASTER_COLUMN_NAMES = new Command<List<String>>() {
        @SuppressWarnings("unchecked")
        @Override
        public List<String> execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {

            final Vector<SeRasterColumn> rasterColumns = connection.getRasterColumns();
            List<String> names = new ArrayList<String>(rasterColumns.size());

            for (SeRasterColumn col : rasterColumns) {
                names.add(col.getQualifiedTableName());
            }
            return names;
        }
    };

    public static final Command<Void> COMMIT_TRANSACTION = new CommitTransaction();

    public static final class CommitTransaction extends Command<Void> {
        @Override
        public Void execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            connection.commitTransaction();
            return null;
        }
    }

    public static final Command<Void> ROLLBACK_TRANSACTION = new Command<Void>() {
        @Override
        public Void execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            connection.rollbackTransaction();
            return null;
        }
    };

    public static final class CloseConnection extends Command<Void> {
        @Override
        public Void execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            try {
                connection.close();
                LOGGER.fine(session.toString() + " successfully closed");
            } catch (Exception ignore) {
                LOGGER.fine("Problem closing " + session.toString() + ": " + ignore.getMessage());
            }
            return null;
        }
    };

    public static final Command<Void> CLOSE_CONNECTION = new CloseConnection();

    public static final Command<List<SeLayer>> GET_LAYERS = new Command<List<SeLayer>>() {
        @SuppressWarnings("unchecked")
        @Override
        public List<SeLayer> execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            return connection.getLayers();
        }
    };

    public static final Command<String> GET_USER = new Command<String>() {
        @Override
        public String execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            return connection.getUser();
        }
    };

    public static final Command<SeRelease> GET_RELEASE = new Command<SeRelease>() {
        @Override
        public SeRelease execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            return connection.getRelease();
        }
    };

    public static final Command<String> GET_DATABASENAME = new Command<String>() {
        @Override
        public String execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            return connection.getDatabaseName();
        }
    };

    public static final Command<SeDBMSInfo> GET_DBMS_INFO = new Command<SeDBMSInfo>() {
        @Override
        public SeDBMSInfo execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            return connection.getDBMSInfo();
        }
    };

    public static final Command<SeInsert> CREATE_SEINSERT = new Command<SeInsert>() {
        @Override
        public SeInsert execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            return new SeInsert(connection);
        }
    };

    public static final Command<SeUpdate> CREATE_SEUPDATE = new Command<SeUpdate>() {
        @Override
        public SeUpdate execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            return new SeUpdate(connection);
        }
    };

    public static final Command<SeDelete> CREATE_SEDELETE = new Command<SeDelete>() {
        @Override
        public SeDelete execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            return new SeDelete(connection);
        }
    };

    public static final class CreateSeRegistrationCommand extends Command<SeRegistration> {
        private String typeName;

        public CreateSeRegistrationCommand(final String typeName) {
            this.typeName = typeName;
        }

        @Override
        public SeRegistration execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            return new SeRegistration(connection, typeName);
        }
    }

    /**
     * 
     * @author groldan
     */
    public static final class PrepareQueryCommand extends Command<SeQuery> {

        private final SeFilter[] spatialConstraints;

        private final SeQueryInfo qInfo;

        private ArcSdeVersionHandler version;

        public PrepareQueryCommand(final SeQueryInfo qInfo, final SeFilter[] spatialConstraints,
                final ArcSdeVersionHandler version) {
            this.qInfo = qInfo;
            this.spatialConstraints = spatialConstraints;
            this.version = version;
        }

        @Override
        public SeQuery execute(ISession session, SeConnection connection) throws SeException,
                IOException {

            final SeQuery seQuery = new SeQuery(connection);

            version.setUpStream(session, seQuery);

            seQuery.prepareQueryInfo(qInfo);

            if (spatialConstraints.length > 0) {
                final boolean setReturnGeometryMasks = false;
                seQuery.setSpatialConstraints(SeQuery.SE_OPTIMIZE, setReturnGeometryMasks,
                        spatialConstraints);
            }

            return seQuery;
        }
    }
}