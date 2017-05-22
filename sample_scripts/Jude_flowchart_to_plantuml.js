//  This script convert Astah flowchart to plantuml fomat text 
//  Author:      Chen Zhi
//  E-mail:      cz_666@qq.com
//  License: APACHE V2.0 (see license file) 
var depth = 0;
var INDENT_STR = 'A'; //2 spaces
var ITEM_MARKER_STR = '* ';

run();

function run() {
    with(new JavaImporter(
            com.change_vision.jude.api.inf.model)) {
        var diagramViewManager = astah.getViewManager().getDiagramViewManager();
        var diagram = diagramViewManager.getCurrentDiagram();
       if (!(diagram instanceof IActivityDiagram)) 
       {
           print('Open a flowchart and run again.');
           return;
       }

        if (!(diagram.isFlowChart() )) 
       {
           print('Open a flowchart and run again.');
           return;
       }

         print('@startuml\n');
         print("title "  + diagram + ' Flowchart\n');
       // print(diagram.isFlowChart() )
        var flow = diagram.getActivity().getFlows();
        var flow_names = new Array();
        var flow_obj = diagram.getActivity().getActivityNodes();
         for (var i in flow_obj) 
         {
            flow_names[i] = INDENT_STR+i;
            var type = "flow_process";

            var stereotypes=flow_obj[i].getStereotypes();
            if(stereotypes.length == 1)
            {
                type = stereotypes[0];
               // print(type);
            }
            //print object define
            {
                print("!define " +INDENT_STR+i + " \"" + flow_obj[i] +"\" as " +INDENT_STR+i + "\n"  );    
            }
         }

        print("(*)-down-> " + flow_names[0] +"\n" );

        //print flowchart logic
        for (var i in flow) {
            var m = flow_obj.indexOf(flow[i].getSource() );
            var n = flow_obj.indexOf(flow[i].getTarget() );
            if(n >= 0)
            {
            print(flow_names[m] +"-->"  );
                if(flow[i].getGuard() != "" )
                {
                    print("["+ flow[i].getGuard() +"] " );
                }
            print( flow_names[n] +"\n"   );            
            }
        }
         print('@enduml');
    }
}
