package com.jci.timetracker.utils;

import java.util.LinkedList;

/**
 * WorkQueue provides easy way to create N threads to do some work in queue. By {@link #execute(Runnable)} can be added a piece of work into queue and it will be automatically processed with any of N threads (ordered by insertion). Mostly you would like to use just one thread.
 * 
 * @author Ladislav Gallay
 * @source http://www.ibm.com/developerworks/library/j-jtp0730/
 * 
 */
public class WorkQueue
{
	/**
	 * Array of all threads that access queue.
	 */
	private final PoolWorker[] threads;
	/**
	 * Queue of all tasks
	 */
	private final LinkedList<Runnable> queue;

	/**
	 * Start worker with given number of threads. They will monitor queue and run tasks in queue.
	 * 
	 * @param nThreads
	 *            Number of threads (at least 1).
	 */
	public WorkQueue(int nThreads)
	{
		queue = new LinkedList<Runnable>();
		threads = new PoolWorker[nThreads];

		for (int i = 0; i < nThreads; i++)
		{
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}

	/**
	 * Add a piece of work into queue
	 * 
	 * @param r
	 *            Runnable task. Use <b>run</b> method to implement the task to be done
	 */
	public void execute(Runnable r)
	{
		synchronized (queue)
		{
			queue.addLast(r);
			queue.notify();
		}
	}

	/**
	 * This class represents one thread. It monitors queue (if is empty, it waits for {@link WorkQueue#execute(Runnable)}.
	 * 
	 * @author Ladislav Gallay
	 * 
	 */
	private class PoolWorker extends Thread
	{
		public void run()
		{
			Runnable r;

			while (true)
			{
				synchronized (queue)
				{
					while (queue.isEmpty())
					{
						try
						{
							queue.wait();
						}
						catch (InterruptedException ignored)
						{
						}
					}

					r = (Runnable) queue.removeFirst();
				}

				// If we don't catch RuntimeException,
				// the pool could leak threads
				try
				{
					r.run();
				}
				catch (RuntimeException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}