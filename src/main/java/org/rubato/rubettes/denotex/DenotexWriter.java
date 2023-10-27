/*
 * Copyright (C) 2002 Gérard Milmeister
 * Copyright (C) 2002 Stefan Müller
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of version 2 of the GNU General Public
 * License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.rubato.rubettes.denotex;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.vetronauta.latrunculus.core.math.yoneda.denotator.ColimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.LimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.map.ModuleMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;

public abstract class DenotexWriter {


    public static PrintStream stream;

    public static void writeDenoToFile(Denotator deno, String filename)
        {
        try{
            FileOutputStream fos = new FileOutputStream(filename);
            stream = new PrintStream(fos);
            stream.println("&begin(substance);");
            stream.println();
            stream.println(deno.getNameString()+":Null@"+deno.getForm().getNameString());
            }
            catch(IOException e){
                    e.printStackTrace();
            }

        if (stream != null)
            {
            write(stream, deno, "  ");
            stream.println("");
            stream.println("&end(substance);");
            stream.close();
            System.out.println("<"+filename+"> written.");
            }
        }

    public static void write(PrintStream out, Denotator denotator, String offset)
        {
        String  tmpString;
        int a, b;

        switch(denotator.getForm().getType())
            {
            case LIMIT:
                out.println(offset+"[");
                for (int i = 0; i<((LimitDenotator)denotator).getFactorCount()-1; i++)
                    {
                    write(out, ((LimitDenotator)denotator).getFactor(i), offset+"  ");
                    if (((LimitDenotator)denotator).getFactor(i).getForm().getType() == FormDenotatorTypeEnum.SIMPLE)
                        out.println(", ");
                    else
                        out.println(offset+"  ,");
                    }
                write(out, ((LimitDenotator)denotator).getFactor(((LimitDenotator)denotator).getFactorCount()-1), offset+"  ");
                out.println();
                out.println(offset+"]");
            break;
            case POWER:
                out.println(offset+"{");
                for (int i = 0; i< ((PowerDenotator)denotator).getFactorCount()-1; i++)
                    {
                    write(out, ((PowerDenotator)denotator).getFactor(i), offset+"  ");
                    if (((PowerDenotator)denotator).getFactor(i).getForm().getType() == FormDenotatorTypeEnum.SIMPLE)
                        out.println(", ");
                    else
                        out.println(offset+"  ,");
                     }
                write(out, ((PowerDenotator)denotator).getFactor(((PowerDenotator)denotator).getFactorCount()-1), offset+"  ");
                out.println();
                out.println(offset+"}");
            break;
            case COLIMIT:
                out.println(offset+"<"+((ColimitDenotator)denotator).getIndex()+",");
                write(out, ((ColimitDenotator)denotator).getFactor(), offset+"  ");
                out.println(offset+">");
            break;
            case SIMPLE:
                ModuleMorphismMap tmpMap = ((ModuleMorphismMap)(denotator.getFrameCoordinate().getMap()));
                //PV.methodPrint("REALVALUE: "+((RElement)tmpMap.getElement()).getValue());
                tmpString = tmpMap.getElement().toString();
                a = tmpString.indexOf("[") + 1;
                b = tmpString.lastIndexOf("]");
                out.print(offset+"("+tmpString.substring(a, b)+")");
            break;
            }
        }


}
