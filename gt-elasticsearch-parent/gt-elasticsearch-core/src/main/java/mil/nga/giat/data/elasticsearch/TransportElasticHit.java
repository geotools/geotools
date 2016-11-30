/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.util.List;
import java.util.Map;

import org.elasticsearch.search.SearchHit;

public class TransportElasticHit extends ElasticHit {

    private SearchHit hit;

    public TransportElasticHit(SearchHit hit) {
        this.hit = hit;
    }

    @Override
    public String getId() {
        return hit.getId();
    }

    @Override
    public String getIndex() {
        return hit.getIndex();
    }

    @Override
    public String getType() {
        return hit.getType();
    }

    @Override
    public Float getScore() {
        return hit.getScore();
    }

    @Override
    public Map<String, Object> getSource() {
        return hit.getSource();
    }

    @Override
    public List<Object> field(String name) {
        return hit.field(name) != null ? hit.field(name).getValues() : null;
    }

}
