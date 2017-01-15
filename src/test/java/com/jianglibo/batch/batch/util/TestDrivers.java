package com.jianglibo.batch.batch.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;
import java.util.stream.Stream;

import org.junit.Test;

public class TestDrivers {

	@Test
	public void t() {
		
		File[] roots = File.listRoots();
		
		Stream.of(roots).forEach(f -> {
			System.out.println(f.getPath());
		});
		
		FileSystem fs = FileSystems.getDefault();
        fs.getFileStores().forEach(fst -> {
        	if (fst.supportsFileAttributeView(DosFileAttributeView.class)) {
        		try {
					System.out.println(fst.getUsableSpace()/(1024*1024*1024));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		System.out.println(fst.name());
        	}
//        	System.out.println("x");
//        	System.out.println(fst.name());
//        	System.out.println(fst.type());
        });
//        File[] drives = File.listRoots();
//        if (drives != null && drives.length > 0) {
//            for (File aDrive : drives) {
//                System.out.println("Drive Letter: " + aDrive);
//                System.out.println("\tType: " + fsv.getSystemTypeDescription(aDrive));
//                System.out.println("\tTotal space: " + aDrive.getTotalSpace());
//                System.out.println("\tFree space: " + aDrive.getFreeSpace());
//                System.out.println();
//            }
//        }
	}
}
