package me.gepron1x.maptags.utlis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.istack.internal.Nullable;


public class MapTagBuilder {
	private boolean isReady = false;
	private HashMap<String,String> input;
	private HashMap<String,Object> result;
    private List<String> keys;
    private int cursor = 0;
public MapTagBuilder(HashMap<String,String> inp) {
	this.input = inp;
	keys = new ArrayList<String>();
	this.result = new HashMap<String,Object>();
	 for (Map.Entry<String, String> entry : inp.entrySet()) {
	             keys.add(entry.getKey());
	    }
}
public String getMessage() {
  return input.get(keys.get(cursor));
}
public void next() {
	if(cursor == keys.size()-1) {
		isReady = true;
		return;
	}
	cursor++;
}
public void setObject(Object inp) {
	result.put(input.get(keys.get(cursor)), inp);
	next();
}
@Nullable
public Map<String,Object> getResult() {
	if(isReady == false) {
		return null;
	}
	return result;
}

}
