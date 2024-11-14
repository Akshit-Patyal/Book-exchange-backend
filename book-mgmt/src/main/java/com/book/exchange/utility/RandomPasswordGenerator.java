package com.book.exchange.utility;

import java.util.UUID;

public class RandomPasswordGenerator {

	public static String randomPasswordGenerator() {
		return UUID.randomUUID().toString().replace("-", "").substring(2, 8);
	}

	public static void main(String[] args) {
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
		System.out.println("uuid = " + uuid.replace("-", "").substring(2, 8));
	}

}
