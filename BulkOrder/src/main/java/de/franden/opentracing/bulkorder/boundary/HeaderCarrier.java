package de.franden.opentracing.bulkorder.boundary;

import io.opentracing.propagation.TextMap;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class HeaderCarrier implements TextMap {
    private MultivaluedMap<String, Object> headers;

    public HeaderCarrier(MultivaluedMap<String, Object> headers) {

        this.headers = headers;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return null;
    }

    @Override
    public void put(String s, String s1) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(s1);
        headers.put(s, objects);

    }
}
