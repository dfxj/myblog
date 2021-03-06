package run.halo.app.controller.admin.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import run.halo.app.exception.BadRequestException;
import run.halo.app.model.enums.MigrateType;
import run.halo.app.model.properties.PrimaryProperties;
import run.halo.app.service.MigrateService;
import run.halo.app.service.OptionService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Migrate controller
 *
 * @author ryanwang
 * @date 2019-10-29
 */
@RestController
@RequestMapping("/api/admin/migrations")
public class MigrateController {

    private final MigrateService migrateService;

    private final OptionService optionService;
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    public MigrateController(MigrateService migrateService,
            OptionService optionService) {
        this.migrateService = migrateService;
        this.optionService = optionService;
    }

    @PostMapping("halo")
    @ApiOperation("Migrate from Halo")
    public void migrateHalo(@RequestPart("file") MultipartFile file) {
        if (optionService.getByPropertyOrDefault(PrimaryProperties.IS_INSTALLED, Boolean.class, false)) {
            throw new BadRequestException("无法在博客初始化完成之后迁移数据");
        }
        EXECUTOR_SERVICE.submit(() -> {
            migrateService.migrate(file, MigrateType.HALO);
        });
    }

    @GetMapping("reimport")
    @ApiOperation("Migrate from Halo")
    public void reimport() {
        EXECUTOR_SERVICE.submit(() -> {
            migrateService.reimport();
        });
    }


    //    @PostMapping("wordpress")
    //    @ApiOperation("Migrate from WordPress")
    //    public void migrateWordPress(@RequestPart("file") MultipartFile file) {
    //        migrateService.migrate(file, MigrateType.WORDPRESS);
    //    }
    //
    //    @PostMapping("cnblogs")
    //    @ApiOperation("Migrate from cnblogs")
    //    public void migrateCnBlogs(@RequestPart("file") MultipartFile file) {
    //        migrateService.migrate(file, MigrateType.CNBLOGS);
    //    }
}
