/*******************************************************************************
 * Copyright 2012
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.core.opennlp;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectSingle;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.PennTree;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.Constituent;
import de.tudarmstadt.ukp.dkpro.core.testing.AssertAnnotations;
import de.tudarmstadt.ukp.dkpro.core.testing.TestRunner;

public class OpenNlpParserTest
{
	@Test
	public void testEnglish()
		throws Exception
	{
		JCas jcas = runTest("en", "chunking", "We need a very complicated example sentence , "
		        + "which contains as many constituents and dependencies as possible .");

        String[] constituentMapped = new String[] { "ADJP 10,26", "ADJP 102,110", "NP 0,2",
                "NP 64,110", "NP 64,98", "NP 8,110", "NP 8,43", "PP 61,110", "PP 99,110",
                "ROOT 0,112", "S 0,112", "S 52,110", "SBAR 46,110", "VP 3,110", "VP 52,110",
                "WHNP 46,51" };

        String[] constituentOriginal = new String[] { "ADJP 10,26", "ADJP 102,110", "NP 0,2",
                "NP 64,110", "NP 64,98", "NP 8,110", "NP 8,43", "PP 61,110", "PP 99,110",
                "ROOT 0,112", "S 0,112", "S 52,110", "SBAR 46,110", "VP 3,110", "VP 52,110",
                "WHNP 46,51" };

		String[] posMapped = new String[] { "PR", "V", "ART", "ADV", "V", "NN", "NN", "PUNC",
				"ART", "V", "PP", "ADJ", "NN", "CONJ", "NN", "PP", "ADJ", "PUNC" };

		String[] posOriginal = new String[] { "PRP", "VBP", "DT", "RB", "VBN", "NN",
				"NN", ",", "WDT", "VBZ", "IN", "JJ", "NNS", "CC",
				"NNS", "IN", "JJ", "." };

		String pennTree = "(ROOT (S (NP (PRP We)) (VP (VBP need) (NP (NP (DT a) (ADJP (RB very) " +
				"(VBN complicated)) (NN example) (NN sentence)) (, ,) (SBAR (WHNP (WDT which)) " +
				"(S (VP (VBZ contains) (PP (IN as) (NP (NP (JJ many) (NNS constituents) (CC and) " +
				"(NNS dependencies)) (PP (IN as) (ADJP (JJ possible)))))))))) (. .)))";

        String[] posTags = new String[] { "#", "$", "''", ",", "-LRB-", "-RRB-", ".", ":", "CC",
                "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS", "LS", "MD", "NN", "NNP", "NNPS",
                "NNS", "PDT", "POS", "PRP", "PRP$", "RB", "RBR", "RBS", "RP", "SYM", "TO", "UH",
                "VB", "VBD", "VBG", "VBN", "VBP", "VBZ", "WDT", "WP", "WP$", "WRB", "``" };

        String[] constituentTags = new String[] { "ADJP", "ADV", "ADVP", "AUX", "CONJP", "FRAG",
                "INTJ", "LST", "NAC", "NEG", "NP", "NX", "O", "PP", "PRN", "PRT", "QP", "S",
                "SBAR", "SQ", "TYPO", "UCP", "UH", "VP", "WHADJP", "WHADVP", "WHNP", "WHPP", "X" };

        String[] unmappedPos = new String[] { "#", "$", "''", "-LRB-", "-RRB-", "``" };

        String[] unmappedConst = new String[] { "ADV", "AUX", "NEG", "O", "TYPO", "UH" };
        
		AssertAnnotations.assertPOS(posMapped, posOriginal, select(jcas, POS.class));
		AssertAnnotations.assertPennTree(pennTree, selectSingle(jcas, PennTree.class));
		AssertAnnotations.assertConstituents(constituentMapped, constituentOriginal, select(jcas, Constituent.class));
        AssertAnnotations.assertTagset(POS.class, "ptb", posTags, jcas);
        AssertAnnotations.assertTagsetMapping(POS.class, "ptb", unmappedPos, jcas);
        AssertAnnotations.assertTagset(Constituent.class, "ptb", constituentTags, jcas);
        AssertAnnotations.assertTagsetMapping(Constituent.class, "ptb", unmappedConst, jcas);
	}

    /**
     * Setup CAS to test parser for the English language (is only called once if an English test is
     * run)
     */
	private JCas runTest(String aLanguage, String aVariant, String aDocument)
		throws Exception
	{
		AnalysisEngineDescription parser = createEngineDescription(OpenNlpParser.class,
				OpenNlpParser.PARAM_VARIANT, aVariant,
				OpenNlpParser.PARAM_PRINT_TAGSET, true,
				OpenNlpParser.PARAM_WRITE_PENN_TREE, true);

		return TestRunner.runTest(parser, aLanguage, aDocument);
	}

	@Rule
	public TestName name = new TestName();

	@Before
	public void printSeparator()
	{
		System.out.println("\n=== " + name.getMethodName() + " =====================");
	}
}
