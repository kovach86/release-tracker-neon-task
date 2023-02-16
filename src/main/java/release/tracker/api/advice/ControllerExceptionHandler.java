package release.tracker.api.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import release.tracker.api.model.api_response.ApiExceptionInfo;
import release.tracker.api.model.api_response.BaseResponseModel;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LogManager.getLogger(ControllerExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleRequestParamsException(WebRequest request, MethodArgumentNotValidException exc) {
        Map<String, String> errorMap = new HashMap<>();
        exc.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public BaseResponseModel handleInternalServerError(HttpServletRequest request, RuntimeException exc) throws JsonProcessingException {
        String errorMessage = "Internal server error occurred -->";
        var errorModel = new ApiExceptionInfo(errorMessage + " " + exc.getMessage());
        errorModel.setEndpointMethod(request.getRequestURI());
        logger.error(new ObjectMapper().writeValueAsString(errorModel));

        return new BaseResponseModel("An error occurred, please be patient while we investigate");
    }
}