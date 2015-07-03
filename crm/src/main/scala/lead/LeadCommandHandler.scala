package lead

import command.{Command, CommandHandler}
import event.{AkkaEventPublisher, DomainEvent}
import lead.LeadCommandHandler.{ConvertLead, LeadCommand}

/**
 * Created by roshansharma on 6/19/15.
 */
class LeadCommandHandler extends CommandHandler[LeadCommand]{

  override def eventPublisher = new AkkaEventPublisher
  override def handleCommand(command: LeadCommand)(implicit context: String) : Unit = command match {
    case e: ConvertLead => {
      val lead = LeadRepository.byId(e.id)
      lead.convert(e.to)
      LeadRepository.save(lead)

      //Publish Event
//      eventPublisher.publish(LeadConverted(e.id, e.to))
    }
  }

}

object LeadCommandHandler {
  //Commands
  sealed trait LeadCommand extends Command
  case class ConvertLead(id: String, to: String)

  //Events
  case class LeadConverted(id: String, to: String) extends DomainEvent
}