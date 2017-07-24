package org.cbioportal.genmone_nexus.annotation.util;

import org.cbioportal.genome_nexus.annotation.domain.MutationAssessor;
import org.cbioportal.genome_nexus.annotation.service.internal.MutationAssessorService;
import org.cbioportal.genome_nexus.annotation.util.Transformer;
import org.junit.Test;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.net.URL;

import static org.junit.Assert.*;

public class MutationAssessorTest
{
    // for debugging
    private static Logger log = Logger.getLogger(String.valueOf(MutationAssessorTest.class));

    // normally taken from application.properties file
    private String url =
        "http://mutationassessor.org/r3/?cm=var&var=VARIANT&frm=json&fts=input,rgaa,rgvt,var,gene,F_impact,F_score,chr,rs_pos";

    @Test
    public void testStringInputs() throws IOException
    {
        MutationAssessorService service = new MutationAssessorService();
        service.setMutationAssessorURL(url);

        String urlString1 = url.replace("VARIANT", "7,140453136,A,T");
        MutationAssessor mutationObj1 = service.getMutationAssessor("7,140453136,A,T", "7:g.140453136A>T");
        MutationAssessor mutationObj2 =
            Transformer.mapJsonToInstance(getReturnString(urlString1), MutationAssessor.class).get(0);

        // getVariant() not tested because variants set manually
        assertEquals(mutationObj1.getHugoSymbol(), mutationObj2.getHugoSymbol());
        assertEquals(mutationObj1.getProteinPosition(), mutationObj2.getProteinPosition());
        assertEquals(mutationObj1.getFunctionalImpact(), mutationObj2.getFunctionalImpact());
        assertEquals(mutationObj1.getFunctionalImpactScore(), mutationObj2.getFunctionalImpactScore(), 0);


        String urlString2 = url.replace("VARIANT", "12,25398285,C,A");
        MutationAssessor mutationObj21 = service.getMutationAssessor("12,25398285,C,A", "12:g.25398285C>A");
        MutationAssessor mutationObj22 =
            Transformer.mapJsonToInstance(getReturnString(urlString2), MutationAssessor.class).get(0);

        assertEquals(mutationObj21.getHugoSymbol(), mutationObj22.getHugoSymbol());
        assertEquals(mutationObj21.getProteinPosition(), mutationObj22.getProteinPosition());
        assertEquals(mutationObj21.getFunctionalImpact(), mutationObj22.getFunctionalImpact());
        assertEquals(mutationObj21.getFunctionalImpactScore(), mutationObj22.getFunctionalImpactScore(), 0);

    }

    @Test
    public void testJunk() throws IOException
    {
        MutationAssessorService service = new MutationAssessorService();
        service.setMutationAssessorURL(url);

        String urlString = url.replace("VARIANT", "junkInput");
        MutationAssessor mutationObj1 = service.getMutationAssessor("junkInput", "junkInput");
        MutationAssessor mutationObj2 =
            Transformer.mapJsonToInstance(getReturnString(urlString), MutationAssessor.class).get(0);

        // getVariant() not tested because variants set manually
        assertEquals(mutationObj1.getHugoSymbol(), mutationObj2.getHugoSymbol());
        assertEquals(mutationObj1.getProteinPosition(), mutationObj2.getProteinPosition());
        assertEquals(mutationObj1.getFunctionalImpact(), mutationObj2.getFunctionalImpact());
        assertEquals(mutationObj1.getFunctionalImpactScore(), mutationObj2.getFunctionalImpactScore(), 0);
    }

    private String getReturnString(String urlString) throws IOException {
        String output = "";
        URL url = new URL(urlString);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")))
        {
            for (String line; (line = reader.readLine()) != null;)
            {
                output += line;
            }
        }
        return output;
    }

}
