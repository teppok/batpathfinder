/* 
    Copyright 2017 Maxpower, Lassi Marttala, lassi.marttala@maxpower.fi

	This file is part of BatPathFinder.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package fi.iki.photon.batmud;

/**
 * Exception to report exceptions from BatPathFinder code
 * @author fizzl
 */
public class BatPathFinderException extends Exception {
    public BatPathFinderException(String msg) {
        super(msg);
    }
}
