package controller;

import infra.network.Protocol;

public interface DefinedController {
    void handler(Protocol recvPt) throws Exception;
}
