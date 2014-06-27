/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.spark;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import spark.Request;
import spark.Response;
import spark.Route;

public class CamelSparkRoute implements Route {

    private final SparkEndpoint endpoint;
    private final Processor processor;

    public CamelSparkRoute(SparkEndpoint endpoint, Processor processor) {
        this.endpoint = endpoint;
        this.processor = processor;
    }

    @Override
    public Object handle(Request request, Response response) {

        Exchange exchange = endpoint.createExchange(ExchangePattern.InOut);
        exchange.getIn().setBody(request);

        try {
            processor.process(exchange);
        } catch (Exception e) {
            exchange.setException(e);
        }

        if (exchange.hasOut()) {
            return exchange.getOut().getBody();
        } else {
            return exchange.getIn().getBody();
        }
    }
}