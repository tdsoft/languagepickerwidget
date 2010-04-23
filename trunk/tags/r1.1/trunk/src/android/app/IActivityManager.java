/*
 *  This file is part of Language Picker Widget.
 *
 *  Language Picker Widget is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Language Picker Widget is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Language Picker Widget.  If not, see <http://www.gnu.org/licenses/>.
 */
package android.app;

import android.content.res.Configuration;
import android.os.RemoteException;

public interface IActivityManager {
	public abstract Configuration getConfiguration () throws RemoteException;
	public abstract void updateConfiguration (Configuration configuration) throws RemoteException;
}
