package me.despical.classicduels.commands.exception;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 12.10.2020
 */
public class CommandException extends Exception {

	private static final long serialVersionUID = 1L;

	public CommandException(String message) {
		super (message);
	}
}