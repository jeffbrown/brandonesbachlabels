package demo

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class StakeController {

    StakeService stakeService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond stakeService.list(params), model:[stakeCount: stakeService.count()]
    }

    def show(Long id) {
        respond stakeService.get(id)
    }

    def create() {
        respond new Stake(params)
    }

    def save(Stake stake) {
        if (stake == null) {
            notFound()
            return
        }

        try {
            stakeService.save(stake)
        } catch (ValidationException e) {
            respond stake.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'stake.label', default: 'Stake'), stake.id])
                redirect stake
            }
            '*' { respond stake, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond stakeService.get(id)
    }

    def update(Stake stake) {
        if (stake == null) {
            notFound()
            return
        }

        try {
            stakeService.save(stake)
        } catch (ValidationException e) {
            respond stake.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'stake.label', default: 'Stake'), stake.id])
                redirect stake
            }
            '*'{ respond stake, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        stakeService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'stake.label', default: 'Stake'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'stake.label', default: 'Stake'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
