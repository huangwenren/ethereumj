package org.ethereum.db;

import org.ethereum.core.Apa;

import java.util.List;
import java.util.UUID;

/**
 * @author: HuShili
 * @date: 2018/8/9
 * @description: none
 */
public interface ApaStore {

    int getApaHashByNumber(UUID apaId);

    int getApaHashByNumber(UUID apaId, int apaHash);

    Apa getApaByNumber(UUID apaId);

    Apa getApaByHash(int hash);

    boolean isApaExist(int hash);

    void saveApa(Apa apa);

    List<Apa> getAllApa();

    void close();
}
