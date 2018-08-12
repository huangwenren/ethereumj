package org.ethereum.db;

import org.ethereum.core.Apa;
import org.ethereum.net.apa.message.ApaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: HuShili
 * @date: 2018/8/9
 * @description: none
 */
@Component
public class ApaStoreImpl implements ApaStore{

    private List<Apa> apas = new ArrayList<>();
    private final static Logger logger = LoggerFactory.getLogger("apastore");

    @Override
    public int getApaHashByNumber(UUID apaId) {
        for (Apa apa : apas) {
            if (apa.getUUID() == apaId){
                return apa.getHash();
            }
        }
        return 0;
    }

    @Override
    public int getApaHashByNumber(UUID apaId, int apaHash) {
        return getApaHashByNumber(apaId);
    }

    @Override
    public Apa getApaByNumber(UUID apaId) {
        for (Apa apa : apas) {
            if (apa.getUUID() == apaId){
                return apa;
            }
        }
        return null;
    }

    @Override
    public Apa getApaByHash(int hash) {
        for (Apa apa : apas) {
            if (apa.getHash() == hash){
                return apa;
            }
        }
        return null;
    }

    @Override
    public boolean isApaExist(int hash) {
        for (Apa apa : apas) {
            if (apa.getHash() == hash){
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveApa(Apa apa) {
        logger.info("Saving apa: " + apa.getMessage().getCommand() + apa.getHash());
        apas.add(apa);
        logger.info("Now saved apa: " + apas.size());
    }

    @Override
    public List<Apa> getAllApa(){
        return apas;
    }

    @Override
    public void close() {

    }
}
