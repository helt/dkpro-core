/*******************************************************************************
 * Copyright 2015
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
package de.tudarmstadt.ukp.dkpro.core.io.brat.internal.model;

import org.apache.commons.lang.StringUtils;

public class BratLabelDecl
{
    private final String type;
    private final String[] labels;
    public BratLabelDecl(String aType, String[] aLabels)
    {
        super();
        type = aType;
        labels = aLabels;
    }
    public String getType()
    {
        return type;
    }
    public String[] getLabels()
    {
        return labels;
    }
    
    @Override
    public String toString()
    {
        return type + " | " + StringUtils.join(labels, " | ");
    }
}
