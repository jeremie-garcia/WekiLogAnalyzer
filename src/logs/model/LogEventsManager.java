package logs.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This classes contains utilities to open and process logFiles. It stores
 * important and frequently reused informations about the logs. It maintains two
 * representations : a hashmaps by logEvent Types and an ArrayList of events
 * sorted in time
 *
 * @author jeremiegarcia
 *
 */
public abstract class LogEventsManager {

	private long beginTime = 0;
	private long endTime = 1000;

	private HashMap<String, ArrayList<LogEvent>> eventsMap;
	private ArrayList<LogEvent> eventsList;

	private File logFile;
	
	private static HashMap<String,ArrayList<LogEvent>> selectedList=new HashMap();

	public static HashMap<String,ArrayList<LogEvent>> getSelectedList(){
		return selectedList;
	}
	
	/**
	 * Process a logFile and extract the data
	 */
	public void setLogFile(File f) {
		if (this.logFile != null && this.logFile.getPath() != f.getPath()) {
			this.reset();
		}
		this.logFile = f;

		if (this.logFile.exists()) {
			this.eventsList = this.extractEventsAsList(this.logFile);
			this.eventsMap = this.createMapFromList(this.eventsList);
			this.updateTimes(this.eventsList);
		}
	}

	private void updateTimes(ArrayList<LogEvent> eventsList2) {
		this.beginTime = eventsList2.get(0).getTimeStamp();
		this.endTime = eventsList2.get(eventsList2.size() - 1).getTimeStamp();
	}

	private HashMap<String, ArrayList<LogEvent>> createMapFromList(ArrayList<LogEvent> eventsList2) {
		HashMap<String, ArrayList<LogEvent>> map = new HashMap<String, ArrayList<LogEvent>>();
		for (LogEvent evt : eventsList2) {
			if (map.containsKey(evt.getLabel())) {
				map.get(evt.getLabel()).add(evt);
			} else {
				ArrayList<LogEvent> list = new ArrayList<LogEvent>();
				list.add(evt);
				map.put(evt.getLabel(), list);
			}
		}
		return map;
	}

	/**
	 * to be implemented by subclasses
	 *
	 * @return
	 */
	protected abstract ArrayList<LogEvent> extractEventsAsList(File logFile);

	private void reset() {
		this.eventsList.clear();
		this.eventsMap.clear();
	}

	public long getBeginTime() {
		return beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public long getDuration() {
		return endTime - beginTime;
	}

	public HashMap<String, ArrayList<LogEvent>> getLogevents() {
		return eventsMap;
	}

	public ArrayList<LogEvent> getTimeSortedLogEventsAsArrayList() {
		return eventsList;
	}

	/*
	public boolean recherchePattern(){
		System.out.println("Ca passe");
		if (selectedList.isEmpty()){
			System.out.println("La liste est vide");
		}
		else{
			ArrayList<String> order=new ArrayList();
			ArrayList<LogEvent> newLigne=new ArrayList();
			ArrayList<LogEventsAggregator> newLigneAggregated = new ArrayList();
			int c=0;
			for (HashMap.Entry<String, ArrayList<LogEvent>> entry : selectedList.entrySet())
			{
			   String key=entry.getKey();
			   ArrayList<LogEvent> evt=entry.getValue();
			   
			   order.add(key);
			}
			
			int a=0;
			System.out.println(order);
			for (LogEvent evt:eventsList){
				String key=order.get(a);
				if(evt.getLabel().equals(key)){
					System.out.println("Trouveeeeeeeeeeeeeeeeeeee");
					newLigne.add(evt);
					a++;
					if(a==order.size())a=0;
				}
				else{
					if(order.contains(evt.getLabel())&& a!=0){
						for(int i=0;i<a;i++){
							System.out.println("ON ennnellelllellleeeeeeeeeeeeeeve");
							newLigne.remove(newLigne.size()-1);
						}
						a=0;
					}						
				}
			}
			for(int i=0;i<a;i++){
				newLigne.remove(newLigne.size()-1);
			}
			System.out.println(newLigne);
			System.out.println(newLigne.size());
			}
			
		return true;
		}

	}
