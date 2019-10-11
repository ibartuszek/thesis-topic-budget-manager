package hu.elte.bm.transactionservice.web.picture;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.bm.transactionservice.service.picture.PictureService;

@RestController
public class PictureController {

    private static final String APPLICATION_JSON = "application/json";

    private final PictureService pictureService;

    @Value("${picture.picture_has_been_saved:Picture has been saved.}")
    private String pictureHasBeenSaved;

    @Value("${picture.picture_has_been_deleted:Picture has been deleted.}")
    private String pictureHasBeenDeleted;

    PictureController(final PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @RequestMapping(value = "/bm/pictures/findById", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public PictureResponse findUserById(@NotNull @RequestParam final Long id, @NotNull @RequestParam final Long userId) {
        return PictureResponse.createSuccessfulPictureResponse(pictureService.findById(id, userId), null);
    }

    @RequestMapping(value = "/bm/pictures/create", method = RequestMethod.POST, produces = APPLICATION_JSON)
    public PictureResponse createPicture(@Valid @RequestBody final PictureContext context) {
        return PictureResponse.createSuccessfulPictureResponse(pictureService.save(context.getPicture(), context.getUserId()), pictureHasBeenSaved);
    }

    @RequestMapping(value = "/bm/pictures/delete", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public PictureResponse deleteTransaction(@Valid @RequestBody final PictureContext context) {
        return PictureResponse.createSuccessfulPictureResponse(pictureService.delete(context.getPicture(), context.getUserId()), pictureHasBeenDeleted);
    }

}
