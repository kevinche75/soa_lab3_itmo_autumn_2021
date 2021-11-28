package ru.itmo.service;

import ru.itmo.utils.ResponseWrapper;

import javax.ejb.Remote;

@Remote
public interface SecondServiceI {

    public ResponseWrapper getDisciplines();
    public ResponseWrapper getDiscipline(String stringId);
    public ResponseWrapper createDiscipline(String stringDiscipline);
    public ResponseWrapper getDisciplineLabWorks(String stringDisciplineId);
    public ResponseWrapper addLabWorkToDiscipline(String stringDisciplineId, String stringLabWorkId);
    public ResponseWrapper removeLabWorkFromDiscipline(String stringDisciplineId, String stringLabWorkId);
    public ResponseWrapper increaseLabWorkDifficulty(String id, String stringSteps);
    public ResponseWrapper test();
}
