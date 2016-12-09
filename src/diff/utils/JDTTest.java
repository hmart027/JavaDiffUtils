package diff.utils;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

public class JDTTest {
	
	public static int getDeltas(String reff, String page){
		List<String> original = new LinkedList<String>();
        List<String> revised = new LinkedList<String>();
		Scanner scan = new Scanner(reff);
		scan.useDelimiter("[\r\n]");
//		System.out.println("\n\nOriginal:");
		while(scan.hasNext()){
			String line = scan.next();
			original.add(line);
//			System.out.println(line);
		}
		scan.close();
		scan = new Scanner(page);
		scan.useDelimiter("[\r\n]");
//		System.out.println("\n\nRevised:");
		while(scan.hasNext()){
			String line = scan.next();
			revised.add(line);
//			System.out.println(line);
		}
		scan.close();
//		System.out.println("\n");
		Patch patch = DiffUtils.diff(original, revised); 
		List<String> deltas = new LinkedList<String>();
		
		int count = 0;
		for (Delta delta: patch.getDeltas()) {
            deltas.add(delta.toString());	
            count += processDelta(delta);
		}
		return count;
	}
	
	public static int processDelta(Delta delta){
        @SuppressWarnings("unchecked")
		List<String> r_lines = (List<String>) delta.getRevised().getLines();
        @SuppressWarnings("unchecked")
		List<String> o_lines = (List<String>) delta.getOriginal().getLines();
//        System.out.println("New Delta:");
//    	System.out.println("Original:");
//        for(String l: o_lines)
//        	System.out.println("\t"+l);
//    	System.out.println("Revised:");
//        for(String l: r_lines)
//        	System.out.println("\t"+l);
        List<Character> o_chars = new LinkedList<Character>();
        List<Character> r_chars = new LinkedList<Character>();
        String o_line = "", r_line = "";
        for(int i = 0; i<o_lines.size(); i++)
        	o_line += o_lines.get(i);
        for(int i = 0; i<r_lines.size(); i++)
        	r_line += r_lines.get(i);
 //   	System.out.println("Errors:");
    	o_chars = Arrays.asList(toCharacterArray(o_line.toLowerCase().toCharArray()));
    	r_chars = Arrays.asList(toCharacterArray(r_line.toLowerCase().toCharArray()));
    	Patch patch = DiffUtils.diff(o_chars, r_chars); 
    	int d_count = 0;
//    	int t_count = 0;
    	for (Delta d: patch.getDeltas()) {
 //   		t_count++;
           @SuppressWarnings("unchecked")
           java.util.List<Character> lines_org = (java.util.List<Character>)((d.getOriginal().getLines()));
           @SuppressWarnings("unchecked")
           java.util.List<Character> lines_rev = (java.util.List<Character>)((d.getRevised().getLines()));

			//System.out.println("\t" + d);
			// System.out.println("\t\t"+d.getOriginal());
			// System.out.println("\t\t"+d.getRevised());

			switch (d.getType()) {
			case CHANGE: {
//				System.out.println("\t" + d);
				d_count += lines_org.size();
				break;
			}
			case INSERT:
				if (!( (lines_rev.size() == 1) && ( lines_rev.get(0).equals(' ') || lines_rev.get(0).charValue() > 127 ) )) {
					d_count += lines_rev.size();
//					System.out.println("\t\t" + d.getOriginal());
//					System.out.println("\t\t" + d.getRevised());
				}
				break;
			case DELETE:
//				System.out.println("\t" + d);
				if (lines_org.get(0).charValue() <= 255)
					d_count += d.getOriginal().size();
				break;
			}
			// if (d.getType().equals(Delta.TYPE.INSERT)){
			// for(Object c: rev.getLines())
			// System.out.print( (int)((Character)c).charValue() + ", ");
			// System.out.println();
			// }
		}
//    	System.out.println(t_count);
//    	System.out.println(d_count);
    	return d_count;
	}
	
	public static Character[] toCharacterArray(char[] in){
		Character[] out = new Character[in.length];
		for(int i = 0; i<in.length; i++)
			out[i] = new Character(in[i]);
		return out;
	}

}
