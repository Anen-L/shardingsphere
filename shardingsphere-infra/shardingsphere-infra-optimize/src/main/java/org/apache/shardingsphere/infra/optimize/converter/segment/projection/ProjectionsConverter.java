/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.infra.optimize.converter.segment.projection;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.shardingsphere.infra.optimize.converter.segment.SQLSegmentConverter;
import org.apache.shardingsphere.infra.optimize.converter.segment.projection.impl.ColumnProjectionConverter;
import org.apache.shardingsphere.infra.optimize.converter.segment.projection.impl.ExpressionProjectionConverter;
import org.apache.shardingsphere.infra.optimize.converter.segment.projection.impl.ShorthandProjectionConverter;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.ColumnProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.ExpressionProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.ProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.ProjectionsSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.ShorthandProjectionSegment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Projection converter.
 */
public final class ProjectionsConverter implements SQLSegmentConverter<ProjectionsSegment, SqlNodeList> {
    
    @Override
    public Optional<SqlNodeList> convert(final ProjectionsSegment segment) {
        Collection<SqlNode> projectionSQLNodes = new ArrayList<>(segment.getProjections().size());
        for (ProjectionSegment each : segment.getProjections()) {
            getProjectionSQLNode(each).ifPresent(projectionSQLNodes::add);
        }
        return Optional.of(new SqlNodeList(projectionSQLNodes, SqlParserPos.ZERO));
    }
    
    private Optional<SqlNode> getProjectionSQLNode(final ProjectionSegment segment) {
        if (segment instanceof ColumnProjectionSegment) {
            return new ColumnProjectionConverter().convert((ColumnProjectionSegment) segment);
        } else if (segment instanceof ExpressionProjectionSegment) {
            return new ExpressionProjectionConverter().convert((ExpressionProjectionSegment) segment);
        } else if (segment instanceof ShorthandProjectionSegment) {
            return new ShorthandProjectionConverter().convert((ShorthandProjectionSegment) segment);
        }
        // TODO process other projection
        return Optional.empty();
    }
}
