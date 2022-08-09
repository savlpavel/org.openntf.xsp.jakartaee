/**
 * Copyright © 2018-2022 Contributors to the XPages Jakarta EE Support Project
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
package org.openntf.xsp.nosql.communication.driver;

import java.util.Optional;
import java.util.stream.Stream;

import org.openntf.xsp.nosql.mapping.extension.impl.ViewKeyQuery;

import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.mapping.Pagination;

public interface DominoDocumentCollectionManager extends DocumentCollectionManager {
	Stream<DocumentEntity> viewEntryQuery(String entityName, String viewName, String category, Pagination pagination, int maxLevel, boolean docsOnly, ViewKeyQuery keyQuery);
	
	Stream<DocumentEntity> viewDocumentQuery(String entityName, String viewName, String category, Pagination pagination, int maxLevel, ViewKeyQuery keyQuery);
	
	void putInFolder(String entityId, String folderName);
	
	void removeFromFolder(String entityId, String folderName);
	
	/**
     * Saves document collection entity
     *
     * @param entity entity to be saved
     * @param computeWithForm whether to compute the document with its form
     * @return the entity saved
     * @throws NullPointerException when document is null
     * @since 2.6.0
     */
    DocumentEntity insert(DocumentEntity entity, boolean computeWithForm);
	
	/**
     * Saves document collection entity
     *
     * @param entity entity to be saved
     * @param computeWithForm whether to compute the document with its form
     * @return the entity saved
     * @throws NullPointerException when document is null
     * @since 2.6.0
     */
    DocumentEntity update(DocumentEntity entity, boolean computeWithForm);
    
    /**
     * Determines whether a document exists with the provided UNID.
     * 
     * @param unid the UNID to check
     * @return {@code true} if a document exists with that UNID; {@code false} otherwise
     * @since 2.7.0
     */
    boolean existsById(String unid);
    
    /**
     * Retrieves a document by its note ID.
     * 
     * @param entityName the entity type to find
     * @param noteId the note ID
     * @return an {@link Optional} describing the entity, or an empty one if no document
     *         by that ID is found
     * @since 2.8.0
     */
    Optional<DocumentEntity> getByNoteId(String entityName, String noteId);
}
