package atdd.station.web;

import atdd.station.application.SubwayLineCommandService;
import atdd.station.application.SubwayLineQueryService;
import atdd.station.application.dto.SubwayLineResponseDto;
import atdd.station.domain.SubwayLine;
import atdd.station.web.dto.SubwayLineCreateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(SubwayLineController.ROOT_URI)
public class SubwayLineController {
    private static final Logger logger = LoggerFactory.getLogger(SubwayLineController.class);

    public static final String ROOT_URI = "/subway-lines";

    private SubwayLineCommandService subwayLineCommandService;
    private SubwayLineQueryService subwayLineQueryService;

    public SubwayLineController(SubwayLineCommandService subwayLineCommandService,
                                SubwayLineQueryService subwayLineQueryService) {

        this.subwayLineCommandService = subwayLineCommandService;
        this.subwayLineQueryService = subwayLineQueryService;
    }

    @PostMapping
    public ResponseEntity<Void> createSubwayLine(@RequestBody SubwayLineCreateRequest subwayLineCreateRequest) {
        logger.info("[SubwayLineController.createSubwayLine] subwayLineCreateRequest={}", subwayLineCreateRequest);

        SubwayLine savedSubwayLine = subwayLineCommandService.create(subwayLineCreateRequest.getName());

        return ResponseEntity.created(URI.create(SubwayLineController.ROOT_URI + "/" + savedSubwayLine.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<SubwayLineResponseDto>> getSubwayLines() {
        return ResponseEntity.ok().body(subwayLineQueryService.getSubwayLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubwayLineResponseDto> getSubwayLine(@PathVariable Long id) {
        logger.info("[SubwayLineController.getSubwayLine] id={}", id);

        SubwayLineResponseDto result = subwayLineQueryService.getSubwayLine(id);

        logger.info("[SubwayLineController.getSubwayLine] result={}", result);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubwayLine(@PathVariable Long id) {
        logger.info("[SubwayLineController.deleteSubwayLine] id={}", id);

        subwayLineCommandService.deleteSubwayLine(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.LOCATION, SubwayLineController.ROOT_URI)
                .build();
    }

}
