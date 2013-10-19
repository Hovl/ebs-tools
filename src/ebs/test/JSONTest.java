package ebs.test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;

import java.util.*;

/**
 * Created by Dubov Aleksey
 * Date: Oct 13, 2009
 * Time: 5:06:45 PM
 * Company: EBS (c) 2009-2011
 */

public class JSONTest extends TestCase {
	private static SomeShit createNewShit(Integer hash) {
		Map<String,String> map = new HashMap<String, String>(2);
		map.put("1" + hash, "1" + hash);
		map.put("2" + hash, "2" + hash);

		return new SomeShit("Device" + hash , "Shitty" + hash, 0 - hash, hash % 2 == 0,
				Arrays.asList("Hi" + hash, "Hallo" + hash, "Fuck off" + hash), map);
	}

	public void testJSONSerialize() throws Exception {
		SomeShit[] originalShits = new SomeShit[100];

		Gson gson = new Gson();

		JsonArray array = new JsonArray();
		for(int i = 0; i < 10; i++) {
			originalShits[i] = createNewShit(i);
			array.add(gson.toJsonTree(originalShits[i], SomeShit.class));
		}

		List<SomeShit> originalShitsList = Arrays.asList(originalShits);

		String jsonStr = gson.toJson(array);
		System.out.println(jsonStr);

		Collection<SomeShit> brandNewArray =
				gson.fromJson(jsonStr, new TypeToken<Collection<SomeShit>>(){}.getType());

		for (SomeShit brandNewShit : brandNewArray) {
			System.out.println(brandNewShit.toString());

			assertTrue("oooopppss!", originalShitsList.contains(brandNewShit));
		}
	}
}
