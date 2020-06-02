package sperlich;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Timer {
	
	public ScheduledFuture<?> timer;
	public Runnable task;
	public int refreshRate;
	public boolean isPaused;
	
	public Timer(int refreshRate, Runnable task) {
		this.task = task;
		this.refreshRate = refreshRate;
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
		timer = ses.scheduleAtFixedRate(this.task, this.refreshRate, this.refreshRate, TimeUnit.MILLISECONDS);
	}
	
	public void pause() {
		timer.cancel(true);
		isPaused = true;
	}
	
	public void resume() {
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
		timer = ses.scheduleAtFixedRate(task, refreshRate, refreshRate, TimeUnit.MILLISECONDS);
		isPaused = false;
	}
	
	public void stop() {
		timer.cancel(true);
	}
	
}
