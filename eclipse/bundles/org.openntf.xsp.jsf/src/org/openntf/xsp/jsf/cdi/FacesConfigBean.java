/**
 * Copyright (c) 2018-2023 Contributors to the XPages Jakarta EE Support Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.xsp.jsf.cdi;

import jakarta.faces.annotation.FacesConfig;

/**
 * Default {@link FacesConfig @FacesConfig} bean to avoid forcing all apps
 * to include one of their own.
 * 
 * @author Jesse Gallagher
 * @since 2.12.0
 */
@FacesConfig
public class FacesConfigBean {

}
