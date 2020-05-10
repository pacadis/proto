package RPCProtocol;

import models.*;

import java.util.stream.StreamSupport;

public class Master{
    public static Protocol.Request CreateLoginRequest(DTOAngajat angajat){
        Protocol.DTOAngajat dtoAngajat = Protocol.DTOAngajat.newBuilder().setUsername(angajat.getUsername()).setPassword(angajat.getPassword()).build();
        Protocol.Request request = Protocol.Request.newBuilder().setType(Protocol.Request.Type.LOGIN).setAngajat(dtoAngajat).build();
        return request;
    }

    public static Protocol.Request CreateLogoutRequest(DTOAngajat angajat){
        Protocol.DTOAngajat dtoAngajat = Protocol.DTOAngajat.newBuilder().setUsername(angajat.getUsername()).setPassword(angajat.getPassword()).build();
        Protocol.Request request = Protocol.Request.newBuilder().setType(Protocol.Request.Type.LOGOUT).setAngajat(dtoAngajat).build();
        return request;
    }

    public static Protocol.Request CreateGetCurrentCurseRequest(){
        Protocol.Request request = Protocol.Request.newBuilder().setType(Protocol.Request.Type.GET_CURRENT_CURSE).build();
        return request;
    }

    public static Protocol.Request CreateSearchByTeamRequest(String team){
        Protocol.Request request = Protocol.Request.newBuilder().setType(Protocol.Request.Type.SEARCH_BY_TEAM).setTeam(team).build();
        return request;
    }

    public static Protocol.Request CreateSubmitInscRequest(DTOInfoSubmit infoSubmit){
        Protocol.InfoSubmit infoSubmit1 = Protocol.InfoSubmit.newBuilder().setCapacitate(infoSubmit.getCapacitate()).setNumeParticipant(infoSubmit.getNumePart()).setNumeEchipa(infoSubmit.getNumeEchipa()).build();
        Protocol.Request request = Protocol.Request.newBuilder().setType(Protocol.Request.Type.SUBMIT_INSC).setInfoSubmit(infoSubmit1).build();
        return request;
    }


    public static CursaDTO[] getCurse(Protocol.Response response){
        CursaDTO[] curse = new CursaDTO[response.getCurseCount()];
        for (int i = 0; i < response.getCurseCount(); i++){
            Protocol.DTOCursa cursa = response.getCurse(i);
            CursaDTO cursa1 = new CursaDTO(cursa.getIdCursa(), cursa.getCapacitate(), cursa.getNrInscrisi());
            curse[i] = cursa1;
        }
        return curse;
    }

    public static ParticipantCursaDTO[] getSearchResult(Protocol.Response response){
        ParticipantCursaDTO[] participanti = new ParticipantCursaDTO[response.getPartCount()];
        for (int i = 0; i < response.getPartCount(); i++){
            Protocol.DTOPart dtoPart = response.getPart(i);
            ParticipantCursaDTO part = new ParticipantCursaDTO(dtoPart.getIdParticipant(), dtoPart.getNumePart(), dtoPart.getCapacitate());
            participanti[i] = part;
        }
        return participanti;
    }

    public static String[] getAllTeams(Protocol.Response response){
        String[] teams = new String[response.getEchipeCount()];
        for (int i = 0; i < response.getEchipeCount(); i++){
            String team = response.getEchipe(i);
            teams[i] = team;
        }
        return teams;
    }

    public static String getError(Protocol.Response response){
        String errorMessage = response.getError();
        return errorMessage;
    }
}