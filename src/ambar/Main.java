package ambar;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Main extends MIDlet implements CommandListener {
	private Display display;
	private Dict dict;
	private Command translate;
	private Command exit;
	private Form form;
	private TextField textField;
	private StringItem result;

	public Main() {
		display = Display.getDisplay(this);
		dict = Dict.getDict();
		form = new Form("Mi Dictionary");
		exit = new Command("Exit", Command.EXIT, 1);
		translate = new Command("Translate", Command.SCREEN, 2);
		textField = new TextField("Enter the word here", "", 20, TextField.ANY);
		result = new StringItem("المعنى:", "", Item.LAYOUT_RIGHT);
		form.append(textField);
		form.append(result);
		form.addCommand(exit);
		form.addCommand(translate);
		form.setCommandListener(this);
	}

	protected void destroyApp(boolean unconditional) {

	}

	protected void pauseApp() {
	}

	protected void startApp() throws MIDletStateChangeException {
		display.setCurrent(form);
	}

	public void commandAction(Command c, Displayable d) {
		if (c == translate) {
			try {
				result.setText(dict.trans(textField.getString()));
			} catch (WordNotFoundException e) {
				result.setText("عفوا، لم يتم إيجاد معنى الكلمة");
			}

		} else if (c == exit) {
			destroyApp(true);
			notifyDestroyed();
		}

	}

}
