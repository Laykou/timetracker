package com.jci.timetracker.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;

/**
 * In requests_log.ser serialized data are stored. Due to renaming of classes (changing package) all class names are incopmatible. Also serialVersionID is not equals when reading someone's other serialized file while debugging. This should be fixed
 * 
 * @author Ladislav Gallay
 * 
 */
public class MigrationSerializerObjectInputStream extends ObjectInputStream
{
	/**
	 * Constructor.
	 * 
	 * @param stream
	 *            input stream
	 * @throws IOException
	 *             if io error
	 */
	public MigrationSerializerObjectInputStream(final InputStream stream) throws IOException
	{
		super(stream);
	}

	@Override
	protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException
	{
		ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();

		if (!classExists(resultClassDescriptor.getName()))
		{
			String replacement = "com.jci.timetracker." + resultClassDescriptor.getName();

			try
			{
				Field f = resultClassDescriptor.getClass().getDeclaredField("name");
				f.setAccessible(true);
				f.set(resultClassDescriptor, replacement);
			}
			catch (Exception e)
			{
				System.err.println("Error while replacing class name." + e.getMessage());
			}
		}

		Class localClass; // the class in the local JVM that this descriptor represents.
		try
		{
			localClass = Class.forName(resultClassDescriptor.getName());
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("No local class for " + resultClassDescriptor.getName());
			return resultClassDescriptor;
		}

		ObjectStreamClass localClassDescriptor = ObjectStreamClass.lookup(localClass);
		if (localClassDescriptor != null)
		{ // only if class implements serializable
			final long localSUID = localClassDescriptor.getSerialVersionUID();
			final long streamSUID = resultClassDescriptor.getSerialVersionUID();

			if (streamSUID != localSUID)
			{ // check for serialVersionUID mismatch.
				final StringBuffer s = new StringBuffer("Overriding serialized class " + localClassDescriptor.getName() + " version mismatch: ");
				s.append("local serialVersionUID = ").append(localSUID);
				s.append(" stream serialVersionUID = ").append(streamSUID);

				Exception e = new InvalidClassException(s.toString());
				System.err.println("Potentially Fatal Deserialization Operation." + e);

				resultClassDescriptor = localClassDescriptor; // Use local class descriptor for deserialization
			}
		}

		return resultClassDescriptor;
	}

	/**
	 * Check if class exists
	 * 
	 * @param className
	 * @return
	 */
	private boolean classExists(String className)
	{
		try
		{
			Class.forName(className);
			return true;
		}
		catch (ClassNotFoundException e)
		{
			return false;
		}
	}
}