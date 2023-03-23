/********************************************************************************
 * Copyright (c) 2021,2022,2023
 *       2022: ZF Friedrichshafen AG
 *       2022: ISTOS GmbH
 *       2022,2023: Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *       2022,2023: BOSCH AG
 * Copyright (c) 2021,2022,2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0. *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/
package org.eclipse.tractusx.irs.component;

import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.GLOBAL_ASSET_ID_REGEX;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MAX_BATCH_SIZE;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MAX_BATCH_SIZE_DESC;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MAX_JOB_TIMEOUT;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MAX_JOB_TIMEOUT_DESC;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MAX_TIMEOUT;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MAX_TIMEOUT_DESC;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MAX_TREE_DEPTH;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MAX_TREE_DEPTH_DESC;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MIN_BATCH_SIZE;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MIN_BATCH_SIZE_DESC;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MIN_JOB_TIMEOUT;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MIN_JOB_TIMEOUT_DESC;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MIN_TIMEOUT;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MIN_TIMEOUT_DESC;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MIN_TREE_DEPTH;
import static org.eclipse.tractusx.irs.component.RegisterBatchOrder.RegisterBatchOrderConstants.MIN_TREE_DEPTH_DESC;

import java.util.List;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.tractusx.irs.component.enums.BatchStrategy;
import org.eclipse.tractusx.irs.component.enums.BomLifecycle;
import org.eclipse.tractusx.irs.component.enums.Direction;
import org.hibernate.validator.constraints.URL;

/**
 * Request body for registering a new Batch Order
 */
@Schema(description = "Request body for registering a new Batch Order.")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings({ "PMD.TooManyStaticImports",
})
public class RegisterBatchOrder {

    @NotEmpty
    @ArraySchema(schema = @Schema(description = "Array of global asset id's.", example = "urn:uuid:6c311d29-5753-46d4-b32c-19b918ea93b0", implementation = String.class, pattern = GLOBAL_ASSET_ID_REGEX), maxItems = Integer.MAX_VALUE)
    private Set<@Pattern(regexp = GLOBAL_ASSET_ID_REGEX) String> globalAssetIds;

    @Schema(description = "BoM Lifecycle of the result tree.", implementation = BomLifecycle.class)
    private BomLifecycle bomLifecycle;

    @ArraySchema(schema = @Schema(implementation = String.class), maxItems = Integer.MAX_VALUE)
    private List<String> aspects;

    @Schema(implementation = Integer.class, minimum = MIN_TREE_DEPTH_DESC, maximum = MAX_TREE_DEPTH_DESC,
            description = "Max depth of the item graph returned. If no depth is set item graph with max depth is returned.")
    @Min(MIN_TREE_DEPTH)
    @Max(MAX_TREE_DEPTH)
    private Integer depth;

    @Schema(implementation = Direction.class, defaultValue = Direction.DirectionConstants.DOWNWARD)
    private Direction direction;

    @Schema(description = "Flag to specify whether aspects should be requested and collected. Default is false.")
    private boolean collectAspects;

    @Schema(description = "Flag to specify whether BPNs should be collected and resolved via the configured BPDM URL. Default is false.")
    private boolean lookupBPNs;

    @URL
    @Schema(description = "Callback url to notify requestor when job processing is finished. There are two uri variable placeholders that can be used: jobId and jobState.",
            example = "https://hostname.com/callback?jobId={jobId}&jobState={jobState}")
    private String callbackUrl;

    @Schema(implementation = Integer.class, minimum = MIN_BATCH_SIZE_DESC, maximum = MAX_BATCH_SIZE_DESC,
            description = "Size of the batch.")
    @Min(MIN_BATCH_SIZE)
    @Max(MAX_BATCH_SIZE)
    private Integer batchSize;

    @Schema(implementation = Integer.class, minimum = MIN_TIMEOUT_DESC, maximum = MAX_TIMEOUT_DESC,
            description = "Timeout in seconds for the complete batch order processing.")
    @Min(MIN_TIMEOUT)
    @Max(MAX_TIMEOUT)
    private Integer timeout;

    @Schema(implementation = Integer.class, minimum = MIN_JOB_TIMEOUT_DESC, maximum = MAX_JOB_TIMEOUT_DESC,
            description = "Timeout in seconds for each job processing inside the complete order.")
    @Min(MIN_JOB_TIMEOUT)
    @Max(MAX_JOB_TIMEOUT)
    private Integer jobTimeout;

    @Schema(implementation = BatchStrategy.class/*, defaultValue = BatchStrategy.PRESERVE_BATCH_JOB_ORDER.name()*/, description = "The strategy how the batch is processed internally in IRS.")
    private BatchStrategy batchStrategy;


    /**
     * Returns requested depth if provided, otherwise MAX_TREE_DEPTH value
     *
     * @return depth
     */
    public int getDepth() {
        return depth == null ? MAX_TREE_DEPTH : depth;
    }

    /**
     * Validation constants
     */
    /* package */ static final class RegisterBatchOrderConstants {
        /* package */ static final String MIN_TREE_DEPTH_DESC = "1";
        /* package */ static final String MAX_TREE_DEPTH_DESC = "100";
        /* package */ static final int MIN_TREE_DEPTH = 1;
        /* package */ static final int MAX_TREE_DEPTH = 100;
        /* package */ static final String MIN_BATCH_SIZE_DESC = "10";
        /* package */ static final String MAX_BATCH_SIZE_DESC = "100";
        /* package */ static final int MIN_BATCH_SIZE = 10;
        /* package */ static final int MAX_BATCH_SIZE = 100;
        /* package */ static final String MIN_TIMEOUT_DESC = "60";
        /* package */ static final String MAX_TIMEOUT_DESC = "86400";
        /* package */ static final int MIN_TIMEOUT = 60;
        /* package */ static final int MAX_TIMEOUT = 86_400;
        /* package */ static final String MIN_JOB_TIMEOUT_DESC = "60";
        /* package */ static final String MAX_JOB_TIMEOUT_DESC = "7200";
        /* package */ static final int MIN_JOB_TIMEOUT = 60;
        /* package */ static final int MAX_JOB_TIMEOUT = 7200;
        /* package */ static final String GLOBAL_ASSET_ID_REGEX = "^urn:uuid:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    }
}
