/*
 * A fabric mod fixing the "Player moved too quickly!" bug
 * Copyright (C) 2022  Oskar Manhart

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package global.oskar.gottagofast;

import global.oskar.gottagofast.config.ConfigHandler;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GottaGoFast implements ModInitializer {

    public static final String MOD_ID = "gottagofast";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    private final ConfigHandler ch = new ConfigHandler();

    @Override
    public void onInitialize() {
        logger.info("GottaGoFast Fabric by PixelAgent007 loaded sucessfully!");
    }
}
