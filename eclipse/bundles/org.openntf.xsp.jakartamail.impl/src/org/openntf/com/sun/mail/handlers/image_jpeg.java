/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.openntf.com.sun.mail.handlers;

import jakarta.activation.ActivationDataFlavor;

import java.awt.Image;

/**
 * DataContentHandler for image/jpeg.
 */
public class image_jpeg extends image_gif {
    private static ActivationDataFlavor[] myDF = {
	new ActivationDataFlavor(Image.class, "image/jpeg", "JPEG Image")
    };

    /**
     * Creates a default {@code image_jpeg}.
     */
    public image_jpeg() {
    }

    @Override
    protected ActivationDataFlavor[] getDataFlavors() {
	return myDF;
    }
}