//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.irs.repositories;

import net.catenax.irs.annotations.ExcludeFromCodeCoverageGeneratedReport;
import net.catenax.irs.entities.PartIdEntityPart;
import net.catenax.irs.entities.PartRelationshipEntity;
import net.catenax.irs.entities.PartRelationshipEntityKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA Repository for managing {@link PartRelationshipEntity} objects.
 */
public interface PartRelationshipRepository extends Repository<PartRelationshipEntity, PartRelationshipEntityKey> {
    /**
     * Call database function (analog to a stored procedure)
     * to recursively retrieve the parts tree from a given part.
     * <p>
     * Note that this method does not validate its parameters, and validation
     * must be performed by the caller.
     *
     * @param oneIDManufacturer    see {@link PartIdEntityPart#getOneIDManufacturer()}.
     *                             Must not be {@literal null} or blank.
     * @param objectIDManufacturer see {@link PartIdEntityPart#getObjectIDManufacturer()}.
     *                             Must not be {@literal null} or blank.
     * @param maxDepth             maximum depth to traverse the tree.
     *                             Must be strictly positive.
     * @return edges in the parts tree below the given part, or an empty list if no edges
     * are found. Guaranteed to never return {@literal null}.
     */
    @Query(nativeQuery = true, value = "SELECT * FROM get_parts_tree(:oneIDManufacturer, :objectIDManufacturer, :maxDepth)")
    List<PartRelationshipEntity> getPartsTree(
            @Param("oneIDManufacturer")
            String oneIDManufacturer,
            @Param("objectIDManufacturer")
            String objectIDManufacturer,
            @Param("maxDepth")
            int maxDepth);
}

/**
 * Substitute repository implementation without DB
 */
@Component
@ExcludeFromCodeCoverageGeneratedReport
class PartRelationshipRepositoryStub implements PartRelationshipRepository {
    @Override
    public List<PartRelationshipEntity> getPartsTree(final String oneIDManufacturer, final String objectIDManufacturer,
          final int maxDepth) {
        return new ArrayList<>();
    }
}


