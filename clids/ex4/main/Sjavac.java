package clids.ex4.main;

import clids.ex4.misc.ToolBox;
import clids.ex4.compile.Manager;
import clids.ex4.exceptions.MessageException;

public class Sjavac {
	public static void main(String[] args) {
		String[] data = null;
		try {
			String fileName = args[0];
			data = ToolBox.importData(fileName);
		} catch (Exception e) {
			System.exit(2);
		}
		Manager manager = new Manager();
		try {
			manager.compile(data);
		} catch (MessageException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		System.exit(0);
	}
}
