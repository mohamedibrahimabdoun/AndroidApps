package android.pkg.list;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListActivity extends Activity {
    /** Called when the activity is first created. */
	String [] Names= new String [] { "Sara","Amr","Moh"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ArrayAdapter<String> adapter= new ArrayAdapter<String> (this,android.R.layout.simple_expandable_list_item_1,Names);
        ListView listview=(ListView) findViewById(R.id.listView);
        listview.setAdapter(adapter);
        listview.setTextFilterEnabled(true);
        listview.setFastScrollEnabled(true);
    }
}