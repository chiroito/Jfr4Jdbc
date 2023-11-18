package dev.jfr4jdbc.interceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockInterceptor<CONTEXT> implements Interceptor<CONTEXT>{
    private List<CONTEXT> preEvents = new ArrayList<CONTEXT>();
    private List<CONTEXT> postEvents = new ArrayList<CONTEXT>();

    @Override
    public void preInvoke(CONTEXT cancelContext) {
        addPreEvent(cancelContext);
    }

    @Override
    public void postInvoke(CONTEXT cancelContext) {
        addPostEvent(cancelContext);
    }

    public void addPreEvent(CONTEXT event){
        preEvents.add(event);
    }

    public List<CONTEXT> getAllPreEvents(){
        return Collections.unmodifiableList(preEvents);
    }

    public void addPostEvent(CONTEXT event){
        postEvents.add(event);
    }

    public List<CONTEXT> getAllPostEvents(){
        return Collections.unmodifiableList(postEvents);
    }
}
