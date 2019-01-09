package com.tagtraum.perf.gcviewer.model;

public class ZGCEvent extends GCEvent
{
	private int capacity;
	private int reserve;
	private int free;
	private int used;
	private int live;
	private int allocated;
	private int garbage;

	public int getCapacity()
	{
		return capacity;
	}

	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
	}

	public int getReserve()
	{
		return reserve;
	}

	public void setReserve(int reserve)
	{
		this.reserve = reserve;
	}

	public int getFree()
	{
		return free;
	}

	public void setFree(int free)
	{
		this.free = free;
	}

	public int getUsed()
	{
		return used;
	}

	public void setUsed(int used)
	{
		this.used = used;
	}

	public int getLive()
	{
		return live;
	}

	public void setLive(int live)
	{
		this.live = live;
	}

	public int getAllocated()
	{
		return allocated;
	}

	public void setAllocated(int allocated)
	{
		this.allocated = allocated;
	}

	public int getGarbage()
	{
		return garbage;
	}

	public void setGarbage(int garbage)
	{
		this.garbage = garbage;
	}
}
