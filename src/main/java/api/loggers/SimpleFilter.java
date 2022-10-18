package api.loggers;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.UrlDecoder;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Optional;

public class SimpleFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFilter.class);

    public SimpleFilter() {
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {

        Response response = ctx.next(requestSpec, responseSpec);

        String uri = UrlDecoder.urlDecode(requestSpec.getURI(),
                Charset.forName(requestSpec
                        .getConfig()
                        .getEncoderConfig()
                        .defaultQueryParameterCharset()),
                true);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n********** REQUEST **********\n");
        stringBuilder.append("URI: ").append(uri).append("\n");
        requestSpec.getHeaders().asList().forEach(header -> stringBuilder.append("Header")
                .append(header.getName()).append("=").append(header.getValue()).append("\n"));
        requestSpec.getQueryParams().forEach((k, v) -> stringBuilder.append("Query: ")
                .append(k).append("=").append(v).append("\n"));
        stringBuilder.append("Request body: \n").append(Optional.ofNullable(requestSpec.getBody())
                .orElse("Request without body\n"));
        stringBuilder.append("\n********** RESPONSE **********\n");
        stringBuilder.append("Status code: ")
                .append(response.statusCode())
                .append("\n");
        stringBuilder.append("Response body: \n")
                .append(response.getBody().prettyPrint());
        LOGGER.info(stringBuilder.toString());

        return response;
    }
}
