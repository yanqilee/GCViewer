package com.tagtraum.perf.gcviewer.imp;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import com.tagtraum.perf.gcviewer.UnittestHelper;
import com.tagtraum.perf.gcviewer.model.GCModel;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.model.GcResourceFile;
import org.junit.Test;

/**
 * Test unified java logging ZGC algorithm in OpenJDK 11
 */
public class TestDataReaderUJLZGC
{
	private GCModel getGCModelFromLogFile(String fileName) throws IOException {
		return UnittestHelper.getGCModelFromLogFile(fileName, UnittestHelper.FOLDER.OPENJDK_UJL, DataReaderUnifiedJvmLogging.class);
	}

	@Test
	public void testGcAll() throws Exception {
		GCModel model = getGCModelFromLogFile("sample-ujl-zgc-all.txt");
		assertThat("size", model.size(), is(10));
		assertThat("amount of gc event types", model.getGcEventPauses().size(), is(0));
		assertThat("amount of gc events", model.getGCPause().getN(), is(0));
		assertThat("amount of full gc event types", model.getFullGcEventPauses().size(), is(3));
		assertThat("amount of full gc events", model.getFullGCPause().getN(), is(3));
		assertThat("amount of concurrent pause types", model.getConcurrentEventPauses().size(), is(7));
	}

	@Test
	public void testDefaultsPauseRelocateStart() throws Exception {
		TestLogHandler handler = new TestLogHandler();
		handler.setLevel(Level.WARNING);
		GCResource gcResource = new GcResourceFile("byteArray");
		gcResource.getLogger().addHandler(handler);
		InputStream in = new ByteArrayInputStream(
			("[1.218s][info][gc,phases] GC(0) Pause Relocate Start 0.679ms")
				.getBytes());

		DataReader reader = new DataReaderUnifiedJvmLogging(gcResource, in);
		GCModel model = reader.read();

		assertThat("number of warnings", handler.getCount(), is(0));
		assertThat("number of events", model.size(), is(1));
		assertThat("pause", model.get(0).getPause(), closeTo(0.000679, 0.00000001));
	}

	@Test
	public void testDefaultsPauseMarkStart() throws Exception {
		TestLogHandler handler = new TestLogHandler();
		handler.setLevel(Level.WARNING);
		GCResource gcResource = new GcResourceFile("byteArray");
		gcResource.getLogger().addHandler(handler);
		InputStream in = new ByteArrayInputStream(
			("[2.946s][info][gc,phases   ] GC(1) Pause Mark Start 0.437ms")
				.getBytes());

		DataReader reader = new DataReaderUnifiedJvmLogging(gcResource, in);
		GCModel model = reader.read();

		assertThat("number of warnings", handler.getCount(), is(0));
		assertThat("number of events", model.size(), is(1));
		assertThat("pause", model.get(0).getPause(), closeTo(0.000437, 0.00000001));
	}

	@Test
	public void testDefaultsPauseMarkEnd() throws Exception {
		TestLogHandler handler = new TestLogHandler();
		handler.setLevel(Level.WARNING);
		GCResource gcResource = new GcResourceFile("byteArray");
		gcResource.getLogger().addHandler(handler);
		InputStream in = new ByteArrayInputStream(
			("[2.959s][info][gc,phases   ] GC(1) Pause Mark End 1.257ms")
				.getBytes());

		DataReader reader = new DataReaderUnifiedJvmLogging(gcResource, in);
		GCModel model = reader.read();

		assertThat("number of warnings", handler.getCount(), is(0));
		assertThat("number of events", model.size(), is(1));
		assertThat("pause", model.get(0).getPause(), closeTo(0.001257, 0.00000001));
	}

	@Test
	public void testDefaultConcurrentMark() throws Exception
	{

	}

	@Test
	public void testDefaultGcWarmup() throws Exception
	{
		TestLogHandler handler = new TestLogHandler();
		handler.setLevel(Level.WARNING);
		GCResource gcResource = new GcResourceFile("byteArray");
		gcResource.getLogger().addHandler(handler);
		InputStream in = new ByteArrayInputStream(
			("[1,048s][info][gc ] GC(1) Garbage Collection (Warmup) 208M(20%)->164M(16%)")
				.getBytes());

		DataReader reader = new DataReaderUnifiedJvmLogging(gcResource, in);
		GCModel model = reader.read();

		assertThat("number of warnings", handler.getCount(), is(0));
		assertThat("number of events", model.size(), is(1));
		assertThat("heap before", model.get(0).getPreUsed(), is(208 * 1024));
		assertThat("heap after", model.get(0).getPostUsed(), is(164 * 1024));
	}

	@Test
	public void testDefaultGcAllocationRate() throws Exception
	{
		TestLogHandler handler = new TestLogHandler();
		handler.setLevel(Level.WARNING);
		GCResource gcResource = new GcResourceFile("byteArray");
		gcResource.getLogger().addHandler(handler);
		InputStream in = new ByteArrayInputStream(
			("[3,167s][info][gc ] GC(3) Garbage Collection (Allocation Rate) 502M(49%)->716M(70%)")
				.getBytes());

		DataReader reader = new DataReaderUnifiedJvmLogging(gcResource, in);
		GCModel model = reader.read();

		assertThat("number of warnings", handler.getCount(), is(0));
		assertThat("number of events", model.size(), is(1));
		assertThat("heap before", model.get(0).getPreUsed(), is(502 * 1024));
		assertThat("heap after", model.get(0).getPostUsed(), is(716 * 1024));
	}

	@Test
	public void testDefaultGcMetadataGCThreshold() throws Exception
	{
		TestLogHandler handler = new TestLogHandler();
		handler.setLevel(Level.WARNING);
		GCResource gcResource = new GcResourceFile("byteArray");
		gcResource.getLogger().addHandler(handler);
		InputStream in = new ByteArrayInputStream(
			("[2.740s][info][gc          ] GC(3) Garbage Collection (Metadata GC Threshold) 958M(0%)->348M(0%)")
				.getBytes());

		DataReader reader = new DataReaderUnifiedJvmLogging(gcResource, in);
		GCModel model = reader.read();

		assertThat("number of warnings", handler.getCount(), is(0));
		assertThat("number of events", model.size(), is(1));
		assertThat("heap before", model.get(0).getPreUsed(), is(958 * 1024));
		assertThat("heap after", model.get(0).getPostUsed(), is(348 * 1024));
	}

	@Test
	public void testDefaultGcProactive() throws Exception
	{
		TestLogHandler handler = new TestLogHandler();
		handler.setLevel(Level.WARNING);
		GCResource gcResource = new GcResourceFile("byteArray");
		gcResource.getLogger().addHandler(handler);
		InputStream in = new ByteArrayInputStream(
			("[2.607s][info][gc          ] GC(4) Garbage Collection (Proactive) 19804M(10%)->20212M(10%)")
				.getBytes());

		DataReader reader = new DataReaderUnifiedJvmLogging(gcResource, in);
		GCModel model = reader.read();

		assertThat("number of warnings", handler.getCount(), is(0));
		assertThat("number of events", model.size(), is(1));
		assertThat("heap before", model.get(0).getPreUsed(), is(19804 * 1024));
		assertThat("heap after", model.get(0).getPostUsed(), is(20212 * 1024));
	}

}