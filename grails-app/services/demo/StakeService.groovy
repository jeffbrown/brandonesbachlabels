package demo

import grails.gorm.services.Service

@Service(Stake)
interface StakeService {

    Stake get(Serializable id)

    List<Stake> list(Map args)

    Long count()

    void delete(Serializable id)

    Stake save(Stake stake)

}