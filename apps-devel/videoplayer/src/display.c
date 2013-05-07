/*
 * MobiAqua Media Player
 *
 * Copyright (C) 2013 Pawel Kolodziejski <aquadran at users.sourceforge.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 */

#include <string.h>

#include "typedefs.h"
#include "logger.h"
#include "display.h"
#include "display_fbdev_priv.h"

display_t *display_get(const char *driver_name) {
	display_t *display = NULL;

	if (driver_name == NULL)
		return NULL;

	if (strcmp(driver_name, "fbdev") == 0) {
		display->init = &display_fbdev_init;
		display->deinit = &display_fbdev_deinit;
		display->configure = &display_fbdev_configure;
	} else
		return NULL;

	return display;
}

void display_release() {
	display_t *display = NULL;

	display->init = NULL;
	display->deinit = NULL;
	display->configure = NULL;
}
