package services.sources.file;

import io.vertx.core.Future;
import io.vertx.core.file.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.Verticle;
import services.models.JsonDispatch;

/**
 * @author Mario Manzanarez
 * @since 7/22/22
 */
public class DynamicJsonFile {
    private static final Logger LOG = LoggerFactory.getLogger(DynamicJsonFile.class);
    private final static FileSystem fileSystem = Verticle.vertxInstance.fileSystem();

    /**
     * Verifica si un archivo existe, de ser así obtiene su información
     * como json y la devuelve
     *
     * @param guiid id del archivo o nombre principal sin extensión
     * @return Futuro con objeto json, información del archivo
     */
    public static Future<JsonDispatch> getJsonByGUIID(Long guiid) {
        String nameFile = guiid + ".json";
        return Future.future(promise -> fileSystem.exists(nameFile)
                .onComplete(completeResult -> {
                            //Verificamos si existe el archivo
                            if (completeResult.succeeded() && completeResult.result()) {
                                // Se lee el archivo y se convierte json
                                fileSystem.readFile(nameFile)
                                        .onSuccess(dataFile -> {
                                            promise.complete(new JsonDispatch(guiid, dataFile.toString()));
                                        })
                                        .onFailure(fail -> {
                                            LOG.error("Error al leer el archivo: " + guiid, fail);
                                            promise.fail(fail);
                                        });
                            } else {
                                LOG.error("Error: el archivo no existe");
                                promise.fail(new Throwable("NO FILE: " + nameFile));
                            }
                        }
                )
        );
    }

}
