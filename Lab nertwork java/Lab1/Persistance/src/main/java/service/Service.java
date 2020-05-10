package service;

import models.*;
import repos.repositories.*;

public class Service {
    private CursaRepository cursaRepository;
    private EchipaRepository echipaRepository;
    private OficiuRepository oficiuRepository;
    private ParticipantRepository participantRepository;
    private InscriereRepository inscriereRepository;

    public Service(CursaRepository cursaRepository, EchipaRepository echipaRepository, OficiuRepository oficiuRepository, ParticipantRepository participantRepository,InscriereRepository inscriereRepository) {
        this.cursaRepository = cursaRepository;
        this.echipaRepository = echipaRepository;
        this.oficiuRepository = oficiuRepository;
        this.participantRepository = participantRepository;
        this.inscriereRepository=inscriereRepository;
    }

    public Iterable<Oficiu> findAllEmployees(){
        return this.oficiuRepository.findAll();
    }
    public void addAngajat(Oficiu angajat){
        this.oficiuRepository.save(angajat);
    }
    public void addCursa(Cursa cursa){
        this.cursaRepository.save(cursa);
    }
    public void addEchipa(Echipa echipa){
        this.echipaRepository.save(echipa);
    }
    public void addParticipant(Participant participant){
        this.participantRepository.save(participant);
    }
    public void addInscriere(Inscriere inscriere){
        this.inscriereRepository.save(inscriere);
    }
    public void deleteAngajat(int id){
        this.oficiuRepository.delete(id);
    }
    public void deleteCursa(int id){
        this.cursaRepository.delete(id);
    }
    public void deleteEchipa(int id){
        this.echipaRepository.delete(id);
    }
    public void deleteParticipant(int id){
        this.participantRepository.delete(id);
    }
    public int angajatiSize(){
        return this.oficiuRepository.size();
    }
    public int curseSize(){
        return this.cursaRepository.size();
    }
    public int echipeSize(){
        return this.echipaRepository.size();
    }
    public int participantiSize(){
        return this.participantRepository.size();
    }
    public void updateAngajat(Oficiu angajatvechi, Oficiu angajatnou){
        this.oficiuRepository.update(angajatvechi.getId(),angajatnou);
    }
    public Iterable<DTOAngajat> findOthersEmployees(DTOAngajat angajat){
        return this.oficiuRepository.findOtherEmployees(angajat);
    }
    public void updateCursa(Cursa cursaveche, Cursa cursanoua){
        this.cursaRepository.update(cursaveche.getId(),cursanoua);
    }
    public void updateEchipa(Echipa echipaveche, Echipa echipanoua){
        this.echipaRepository.update(echipaveche.getId(),echipanoua);
    }
    public void updateParticipant(Participant participantvechi, Participant participantnou){
        this.participantRepository.update(participantvechi.getId(),participantnou);
    }

    public boolean login(String username,String parola){
        return oficiuRepository.localLogin(username,parola);
    }

    public Iterable<Cursa> findAllCursa(){
        return cursaRepository.findAll();
    }

    public Iterable<CursaDTO> findlAllCursaDto(){
        return cursaRepository.findCursaNrParticipanti();
    }

    public Iterable<ParticipantCursaDTO> findAllParticipantiCursaDto(String t){
        return participantRepository.findByTeam(t);
    }

    public Integer findIdCursa(Integer cap){
        return cursaRepository.findIdCursa(cap);
    }

    public void addInscriereParticipant(String nume,String numeEchipa,Integer capacitate){
        Integer idParticipant=participantRepository.getNewIdParticipant()+1;
        Integer idInscriere=inscriereRepository.getNewIdInscriere()+1;
        Integer idCursa=cursaRepository.findIdCursa(capacitate);
        Integer idEchipa=echipaRepository.findIdEchipa(numeEchipa);
        //Integer i=inscriereRepository.findIdInscriere(idParticipant,idCursa);
        Participant pnou = new Participant(idParticipant,nume,idEchipa);
        this.addParticipant(pnou);
        Inscriere inoua = new Inscriere(idInscriere,idParticipant,idCursa);
        this.addInscriere(inoua);
        }

}

