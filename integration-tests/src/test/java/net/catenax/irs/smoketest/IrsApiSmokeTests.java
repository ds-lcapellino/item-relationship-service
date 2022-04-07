//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.irs.smoketest;

import net.catenax.irs.dtos.PartRelationshipsWithInfos;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static net.catenax.irs.dtos.ItemsTreeView.AS_BUILT;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke tests verify that the cloud infrastructure where IRS runs is working as expected
 * @see <a href="https://confluence.catena-x.net/display/ARTI/MTPDC+Testing">MTPDC Testing</a>
 */
@Tag("SmokeTests")
public class IrsApiSmokeTests extends SmokeTestsBase {

    @Test
    public void getPartsTreeByVin_success() {

        var response =
            given()
                .spec(getRequestSpecification())
                .baseUri(irsApiUri)
                .pathParam(VIN, SAMPLE_VIN)
                .queryParam(VIEW, AS_BUILT)
            .when()
                .get(PATH_BY_VIN)
            .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().as(PartRelationshipsWithInfos.class);

        assertThat(response.getRelationships()).isNotEmpty();
        assertThat(response.getPartInfos()).isNotEmpty();
    }

}
