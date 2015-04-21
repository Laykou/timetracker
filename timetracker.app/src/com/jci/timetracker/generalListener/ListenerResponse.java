package com.jci.timetracker.generalListener;

/**
 * Definuje novu odpoved (akciu) po vyvolani daneho listenera. Reaguje na akciu, ktora je dana v Type. Povinne treba implementovat metodu <b>call</b>.
 * 
 * @author Laykou
 * @param <Type>
 *            - typ vyvolanej udalosti
 */

public interface ListenerResponse<Type>
{
	/**
	 * @param e
	 *            - parameter je typu Type, ktory je definovany podla implementacie tohto rozhrania. Obsahuje detaily vyvolanej akcie podla typu Type.
	 */
	public abstract void call(Type e);
}
