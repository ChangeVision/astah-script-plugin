//  This script convert Astah SequenceDiagram to plantuml fomat text 
//  Author:      Chen Zhi
//  E-mail:      cz_666@qq.com
//  License: APACHE V2.0 (see license file) 
var depth = 0;
var INDENT_STR = 'A'; //2 spaces
var ITEM_MARKER_STR = '* ';

run();

function sortNumber(a, b)
{
//    print((a.getIndex()) + '-' + (b.getIndex())+'='+ (parseInt(a.getIndex()*10) - parseInt(b.getIndex()*10)) + '\n' ); 
    return  parseInt(a.getIndex()*10) - parseInt(b.getIndex()*10);
}

function run() {
    with(new JavaImporter(
            com.change_vision.jude.api.inf.model)) {
        var diagramViewManager = astah.getViewManager().getDiagramViewManager();
        var diagram = diagramViewManager.getCurrentDiagram();


       if (!(diagram instanceof ISequenceDiagram)) 
       {
           print('Open a ISequenceDiagram and run again.');
           return;
       }


         //print(diagram + ' Sequence\n');
         print('@startuml\n');
       // print(diagram.isFlowChart() )
        var lifelines = diagram.getInteraction().getLifelines();
        var gates = diagram.getInteraction().getGates();
        var msgs= diagram.getInteraction().getMessages();

        var objnames = new Array();
        var objs = new Array();
        var m = 0;
        
         for (var i in lifelines) 
         {
            // print(lifelines[i].getName() + ":" + lifelines[i].getBase() +": "  );

             if( lifelines[i].getBase()!= null )
             {
                objnames[i] = lifelines[i].getName() + "_" + lifelines[i].getBase();
             }
             else
             {
                 objnames[i] = lifelines[i].getName();
             }
     
              print( "participant "+ objnames[i] +'\n' );
         }

        //sort msg by index
        var msgs2 = new Array();
        var msg_reply = new Array(); //reply message, reply massage  no index
        var msg_reply_act = new Array();
        var reply_n = 0;
        for (var i in msgs) 
         {//find reply message
                if( msgs[i].isReturnMessage() )
                {
                       msg_reply[reply_n] = msgs[i];
                       msg_reply_act[reply_n] =  msgs[i].getActivator();
                       reply_n++;
                }
         }

         msgs2 = msgs;
    
     msgs2.sort(sortNumber) ;

         for (var i in msgs2) 
         {
             if(msgs2[i] == undefined || msgs[i].isReturnMessage() )
             {
                 continue;
             }

            var m = lifelines.indexOf(msgs2[i].getSource() );
            var n = lifelines.indexOf(msgs2[i].getTarget() );
            var arrow = " -> " ;
            if( msgs2[i].isSynchronous() )
            {
                arrow = " -> " ;
            }
            
            if( msgs2[i].isAsynchronous() )
            {
                 arrow = " ->> " ;
            }

            print( objnames[m] + arrow +objnames[n] + ':'+ msgs2[i].getIndex()+'.'+ msgs2[i] +'\n');

            //print reply
            var k = msg_reply_act.indexOf(msgs2[i] );
            if(k >=0 )
            {
                var m1 = lifelines.indexOf(msg_reply[k].getSource() );
                var n1 = lifelines.indexOf(msg_reply[k].getTarget() );
                print( objnames[m1] + ' -->> ' +objnames[n1] + ':'+ 'reply.'+ msg_reply[k] +'\n');                
            }
         }
         print('@enduml');
    }
}
