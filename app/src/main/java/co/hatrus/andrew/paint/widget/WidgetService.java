package co.hatrus.andrew.paint.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new NoteViewsFactory(this.getApplicationContext(),
                intent));
    }
}