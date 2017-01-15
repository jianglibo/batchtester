package com.jianglibo.batch.batch.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.IntStream;

import com.google.common.base.Strings;
import com.jianglibo.batch.batch.TbatchBase;

public class FixtureUtil {

	public static Path createFixture(String subFolder, String ext, int itemNumbers) throws IOException {
		Path pp = Paths.get(TbatchBase.BATCH_FIXTURE_BASE).resolve(subFolder);
		if (!Files.exists(pp)) {
			pp.toFile().mkdirs();
		}
		
		String fn = UUID.randomUUID().toString();
		
		if (!Strings.isNullOrEmpty(ext)) {
			if (ext.startsWith(".")) {
				fn = fn + ext;
			} else {
				fn = fn + "." + ext;
			}
		}
		
		Path datap = pp.resolve(fn);
		PrintWriter pw = new PrintWriter(datap.toFile());
		IntStream.range(0, itemNumbers).mapToObj(i -> UUID.randomUUID().toString() + "," + UUID.randomUUID().toString()).forEach(s -> pw.println(s));
		pw.flush();
		pw.close();
		return datap;
	}

	public static void clearFolder(String subFolder) throws IOException {
		Path pp = Paths.get(TbatchBase.BATCH_FIXTURE_BASE).resolve(subFolder);
		Files.list(pp).forEach(p -> {
			try {
				Files.delete(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
