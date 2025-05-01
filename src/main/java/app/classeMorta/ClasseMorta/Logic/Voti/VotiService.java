package app.classeMorta.ClasseMorta.Logic.Voti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotiService {

    private final VotiRepository votiRepository;

    @Autowired
    public VotiService(VotiRepository votiRepository) {
        this.votiRepository = votiRepository;
    }

    public List<Voti> getVotiPerMateriaEID(Long idMateria, Long idStudente){
        return votiRepository.findAllByStudenteIdAndMateriaId(idStudente, idMateria);
    }

}
