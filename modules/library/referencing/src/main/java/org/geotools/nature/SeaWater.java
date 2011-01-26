/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 1999-2008, Open Source Geospatial Foundation (OSGeo)
 *  
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    NOTE: permission has been given to the JScience project (http://www.jscience.org)
 *          to distribute this file under BSD-like license.
 */
package org.geotools.nature;


/**
 * Sea water properties as a function of salinity, temperature and pressure.
 * Density is computed using the 1980 definition of Equation of State (EOS80).
 * Units are:
 *
 * <ul>
 *   <li>Salinity: Pratical Salinity Scale 1978 (PSS-78).</li>
 *   <li>Temperature: Celsius degrees according International Temperature Scale 1968 (ITS-68).</li>
 *   <li>Pressure: decibars (1 dbar = 10 kPa).
 * </ul>
 *
 * @source $URL$
 * @version $Id$
 * @author Bernard Pelchat
 * @author Martin Desruisseaux (PMO, IRD)
 *
 * @since 2.1
 */
public final class SeaWater {
    /*
     * Note: Les algorithmes originaux de l'UNESCO recevait en entrés
     *       des pressions en décibars. Les algorithmes écrites par
     *       Bernard Pelchat recevaient en entrés des pressions en
     *       MegaPascal. La première ligne de code des algorithmes
     *       de Bernard Pelchat multipliait donc les pressions par
     *       100, afin de les convertir en decibars.
     */

    /**
     * Conductivity (in mS/cm) of a standard sea water sample.
     * S is for <cite>Siemens</cite> (or Mho, its the same...).
     */
    public static final double STANDARD_CONDUCTIVITY=42.914;

    /**
     * Coéfficients de l'équation d'état EOS-80. La densité
     * calculée par ces coéfficients est la densité Sigma-T.
     */
    private static final double
        EOS80_A[]  = { -28.263737E+0  ,  6.793952E-2  ,  -9.095290E-3  ,  1.001685E-4  ,  -1.120083E-6  ,  6.536332E-9 },
        EOS80_B[]  = {   8.24493E-1   , -4.0899E-3    ,   7.6438E-5    , -8.2467E-7    ,   5.3875E-9 },
        EOS80_C[]  = {  -5.72466E-3   ,  1.0227E-4    ,  -1.6546E-6 },
        EOS80_D    =     4.8314E-4,
        EOS80_E[]  = { -1930.06E+0    , 148.4206E+0   ,  -2.327105E+0  ,  1.360477E-2  ,  -5.155288E-5 },
        EOS80_F[]  = {  54.6746E+0    ,  -6.03459E-1  ,   1.09987E-2   , -6.1670E-5 },
        EOS80_G[]  = {   7.944E-2     ,   1.6483E-2   ,  -5.3009E-4 },
        EOS80_H[]  = {  -1.194975E-1  ,   1.43713E-3  ,   1.16092E-4   , -5.77905E-7 },
        EOS80_I[]  = {   2.2838E-3    ,  -1.0981E-5   ,  -1.6078E-6 },
        EOS80_J    =     1.91075E-4,
        EOS80_K[]  = {   3.47718E-5   ,  -6.12293E-6  ,   5.2787E-8 },
        EOS80_M[]  = {  -9.9348E-7    ,   2.0816E-8   ,   9.1697E-10 },
        EOS80_N[]  = {   21582.27     ,   3.359406    ,   5.03217E-5 },
        RHO_35_0_0 =  1028.1063,
        DR_35_0_0  =  28.106331;

    /**
     * Coéfficients de l'équation d'état EOS-80. La densité
     * calculée par ces coéfficients est la densité "vrai".
     */
    private static final double
        EOS80_At[] = {999.842594  , 6.793952E-2 , -9.095290E-3 , 1.001685E-4 , -1.120083E-6 , 6.536332E-9 },
        EOS80_Et[] = {19652.21    , 148.4206    , -2.327105    , 1.360477E-2 , -5.155288E-5 },
        EOS80_Ht[] = { 3.239908   , 1.43713E-3  , 1.16092E-4   , -5.77905E-7 },
        EOS80_Kt[] = { 8.50935E-5 , -6.12293E-6 , 5.2787E-8 };

    /**
     * Coéfficients de l'équation de la salinité PSS-78.
     */
    private static final double
        PSS78_A[] = { 0.0080    , -0.1692     , 25.3851      , 14.0941    , -7.0261    ,  2.7081 },
        PSS78_B[] = { 0.0005    , -0.0056     , -0.0066      , -0.0375    ,  0.0636    , -0.0144 },
        PSS78_C[] = { 0.6766097 ,  2.00564E-2 ,  1.104259E-4 , -6.9698E-7 ,  1.0031E-9           },
        PSS78_D[] = { 3.426E-2  ,  4.464E-4   ,  4.215E-1    , -3.107E-3                         },
        PSS78_E[] = { 2.070E-5  , -6.370E-10  ,  3.989E-15                                       },
        PSS78_G[] = {-0.1692    , 50.7702     , 42.2823      ,-28.1044    , 13.5405              },
        PSS78_H[] = {-0.0056      -0.0132     , -0.1125      ,  0.2544    , -0.0720              },
        PSS78_K   = 0.0162;

    /**
     * Coéfficients pour les salinités élevées,
     */
    private static final double
        PSS78_AR[] = {7.737,        -9.819,     8.663,      -2.625},
        PSS78_AT[] = {3.473E-2, 3.188E-3,   -4.655E-5         },
        PSS78_CR[] = {-10.01E-2,    4.82E-2,    -6.682E-4         };

    /**
     * Constantes nécessaires au calcul de la chaleur spécifique.
     *
     * @see #specificHeat
     */
    private static final double
        HEAT_AA[] = { -7.643575  ,  0.1072763  , -1.38385E-3 },
        HEAT_BB[] = { 0.1770383  , -4.07718E-3 ,  5.148E-5 },
        HEAT_CC[] = { 4217.4     , -3.720283   ,  0.1412855  , -2.654387E-3 ,  2.093236E-5 },
        HEAT_A[]  = { -4.9592E-1 ,  1.45747E-2 , -3.13885E-4 ,  2.0357E-6   ,  1.7168E-8 },
        HEAT_B[]  = {  2.4931E-4 , -1.08645E-5 ,  2.87533E-7 , -4.0027E-9   ,  2.2956E-11 },
        HEAT_C[]  = { -5.422E-8  ,  2.6380E-9  , -6.5637E-11 ,  6.136E-13 },
        HEAT_D[]  = {  4.9247E-3 , -1.28315E-4 ,  9.802E-7   ,  2.5941E-8   , -2.9179E-10 },
        HEAT_E[]  = { -1.2331E-4 , -1.517E-6   ,  3.122E-8 },
        HEAT_F[]  = { -2.9558E-6 ,  1.17054E-7 , -2.3905E-9  ,  1.8448E-11 },
        HEAT_G    =    9.971E-8,
        HEAT_H[]  = {  5.540E-10 , -1.7682E-11 , 3.513E-13 },
        HEAT_J    =   -1.4300E-12;

    /**
     * Constantes nécessaires au calcul de la température adiabétique.
     *
     * @see #adiabeticTemperatureGradient
     */
    private static final double
        GRAD_A[] = { 3.5803E-05 ,  8.5258E-06 , -6.8360E-08 ,  6.6228E-10 },
        GRAD_B[] = { 1.8932E-06 , -4.2393E-08 },
        GRAD_C[] = { 1.8741E-08 , -6.7795E-10 ,  8.7330E-12 , -5.4481E-14 },
        GRAD_D[] = {-1.1351E-10 ,  2.7759E-12 },
        GRAD_E[] = {-4.6206E-13 ,  1.8676E-14 , -2.1687E-16 };

    /**
     * Constantes nécessaires au calcul de la profondeur.
     *
     * @see #depth
     */
    private static final double
        DEPTH_C[] = {9.72659 , -2.2512E-5 , 2.279E-10 , -1.82E-15};

    /**
     * Constantes nécessaires au calcul de la vitesse du son.
     *
     * @see #soundVelocity
     */
    private static final double
        SOUND_A0[] = { 1.389     , -1.262E-2  ,   7.164E-5   ,  2.006E-6   ,  -3.21E-8 },
        SOUND_A1[] = { 9.4742E-5 , -1.2580E-5 ,  -6.4885E-8  ,  1.0507E-8  ,  -2.0122E-10 },
        SOUND_A2[] = {-3.9064E-7 ,  9.1041E-9 ,  -1.6002E-10 ,  7.988E-12 },
        SOUND_A3[] = { 1.100E-10 ,  6.649E-12 ,  -3.389E-13 },
        SOUND_B0[] = {-1.922E-2  , -4.42E-5 },
        SOUND_B1[] = { 7.3637E-5 ,  1.7945E-7 },
        SOUND_C0[] = {1402.388   ,  5.03711   ,  -5.80852E-2 ,  3.3420E-4 , -1.47800E-6  , 3.1464E-9 },
        SOUND_C1[] = {0.153563   ,  6.8982E-4 ,  -8.1788E-6  ,  1.3621E-7 , -6.1185E-10 },
        SOUND_C2[] = {3.1260E-5  , -1.7107E-6 ,   2.5974E-8  , -2.5335E-10,  1.0405E-12 },
        SOUND_C3[] = {-9.7729E-9 ,  3.8504E-10,  -2.3643E-12 },
        SOUND_D0   =   1.727E-3,
        SOUND_D1   =  -7.9836E-6;

    /**
     * Constantes nécessaires au calcul de la saturation en oxygène dissous.
     *
     * @see #saturationO2
     */
    private static final double
        O2_AT[] = {-135.29996, 1.572288E+5, -6.637149E+7, 1.243678E+10, -8.621061E+11},
        O2_AS[] = {0.020573, -12.142, 2363,1};




    /**
     * Do not allow instantiation of this class.
     */
    private SeaWater(){
    }

    /**
     * Computes density as a function of salinity, temperature and pressure.
     *
     * @param S Salinity PSS-78 (0 to 42)
     * @param T Temperature ITS-68 (-2 to 40°C)
     * @param P Pressure in decibars (0 to 10<sup>5</sup> dbar), not including atmospheric pressure.
     * @return  Density (kg/m³).
     */
    public static double density(final double S, final double T, double P) {
        P /= 10.0;

        // Pure water density at atmospheric pressure
        final double RHO_0_T_0 = polynome(T,EOS80_At);

        // Sea water density at atmospheric pressure
        final double SR = Math.sqrt(S);
        final double RHO_S_T_0 = (EOS80_D*S + polynome(T,EOS80_C)*SR + polynome(T,EOS80_B))*S + RHO_0_T_0;

        // Compression terms
        final double K_S_T_0 = (polynome(T,EOS80_F) + polynome(T,EOS80_G)*SR)*S + polynome(T,EOS80_Et);
        final double K_S_T_P = K_S_T_0 + ((EOS80_J*SR + polynome(T,EOS80_I)) * S + polynome(T,EOS80_Ht) +
                               (polynome(T,EOS80_Kt) + polynome(T,EOS80_M) * S) * P) * P;
        return RHO_S_T_0/( 1.0 - P/K_S_T_P );
    }

    /**
     * Computes density sigma-T as a function of salinity, temperature and pressure.
     * Density Sigma-T is equivalent to the true density minus 1000&nbsp;kg/m³, and
     * has typical values around 35. This computation avoid some rouding errors
     * occuring in the true density computation.
     *
     * @param S Salinity PSS-78 (0 to 42)
     * @param T Temperature ITS-68 (-2 to 40°C)
     * @param P Pressure in decibars (0 to 10<sup>5</sup> dbar), not including atmospheric pressure.
     * @return  Density Sigma-T (kg/m³).
     */
    public static double densitySigmaT(final double S, final double T, double P) {
        P /= 10.0;
        // Sea water density at atmospheric pressure
        final double SR = Math.sqrt(S);
        final double RHO = (EOS80_D*S + polynome(T,EOS80_C)*SR + polynome(T,EOS80_B))*S + polynome(T,EOS80_A);

        // Specific volume at atmospheric pressure
        final double V_35_0_0    = 1.0/RHO_35_0_0;
        final double SVAN_S_T_0  = -RHO*V_35_0_0/(RHO+RHO_35_0_0);
        if (P <= 0) {
            return RHO + DR_35_0_0;
        }
        // Compression terms, DK = K(S,T,P) - K(35,0,P)
        final double K0 = (polynome(T,EOS80_F) + polynome(T,EOS80_G)*SR)*S + polynome(T,EOS80_E);
        final double DK = K0 + (((EOS80_J * SR + polynome(T,EOS80_I)) * S + polynome(T,EOS80_H)) +
                          (polynome(T,EOS80_K) + polynome(T,EOS80_M) * S) * P) * P;

        final double K_35_0_P = polynome(P,EOS80_N);
        final double V_S_T_0  = SVAN_S_T_0 + V_35_0_0;
        final double SVANS    = SVAN_S_T_0 * (1.0 - P/K_35_0_P) + V_S_T_0 * P * DK /
                                (K_35_0_P * (K_35_0_P + DK));

        // Compute density anomaly
        final double V_35_0_P  = V_35_0_0*( 1.0 - P/K_35_0_P );
        final double DR_35_0_P = P/(K_35_0_P*V_35_0_P);
        final double DVAN      = SVANS/( V_35_0_P*( V_35_0_P + SVANS ) );
        return DR_35_0_0 + DR_35_0_P - DVAN;
    }

    /**
     * Computes volume as a function of salinity, temperature and pressure.
     * This quantity if the inverse of density. This method is equivalent
     * to <code>1/{@link #density density}(S,T,P)</code>.
     *
     * @param S Salinity PSS-78 (0 to 42)
     * @param T Temperature ITS-68 (-2 to 40°C)
     * @param P Pressure in decibars (0 to 10<sup>5</sup> dbar), not including atmospheric pressure.
     * @return  Volume (m³/kg).
     */
    public static double volume(final double S, final double T, double P) {
        P /= 10.0;
        // Sea water density at atmospheric pressure
        final double SR = Math.sqrt(S);
        final double RHO = (EOS80_D*S + polynome(T,EOS80_C)*SR + polynome(T,EOS80_B))*S + polynome(T,EOS80_A);

        // Specific volume at atmospheric pressure
        final double V_35_0_0    = 1.0/RHO_35_0_0;
        final double SVAN_S_T_0  = -RHO*V_35_0_0/(RHO+RHO_35_0_0);
        if (P <= 0) {
            return SVAN_S_T_0 + V_35_0_0;
        }
        // Compression terms, DK = K(S,T,P) - K(35,0,P)
        final double K0 = (polynome(T,EOS80_F) + polynome(T,EOS80_G) * SR) * S + polynome(T,EOS80_E);
        final double DK = K0 + (((EOS80_J * SR + polynome(T,EOS80_I)) * S + polynome(T,EOS80_H)) +
                          (polynome(T,EOS80_K) + polynome(T,EOS80_M) * S) * P) * P;

        final double K_35_0_P = polynome(P,EOS80_N);
        final double V_S_T_0  = SVAN_S_T_0 + V_35_0_0;
        return (SVAN_S_T_0 + V_35_0_0) * (1.0 - P/K_35_0_P) + V_S_T_0 * P * DK / (K_35_0_P * (K_35_0_P + DK));
    }

    /**
     * Computes volumic anomaly as a function of salinity, temperature and pressure.
     * Volumic anomaly is defined as the sea water sample's volume minus a standard
     * sample's volume, where the standard sample is a sample of salinity 35, temperature
     * 0°C and the same pressure. In pseudo-code, {@code volumeAnomaly} is equivalent
     * to <code>{@link #volume volume}(S,T,P)-{@link #volume volume}(35,0,P)</code>.
     *
     * @param S Salinity PSS-78 (0 to 42)
     * @param T Temperature ITS-68 (-2 to 40°C)
     * @param P Pressure in decibars (0 to 10<sup>5</sup> dbar), not including atmospheric pressure.
     * @return  Volumic anomaly (m³/kg).
     */
    public static double volumeAnomaly(final double S, final double T, double P) {
        P /= 10.0;
        // Sea water density at atmospheric pressure
        final double SR = Math.sqrt(S);
        final double RHO = (EOS80_D*S + polynome(T,EOS80_C)*SR + polynome(T,EOS80_B))*S + polynome(T,EOS80_A);

        // Specific volume at atmospheric pressure
        final double V_35_0_0    = 1.0/RHO_35_0_0;
        final double SVAN_S_T_0  = -RHO*V_35_0_0/(RHO+RHO_35_0_0);
        if (P <= 0) {
            return SVAN_S_T_0;
        }
        // Compression terms, DK = K(S,T,P) - K(35,0,P)
        final double K0 = (polynome(T,EOS80_F) + polynome(T,EOS80_G)*SR)*S + polynome(T,EOS80_E);
        final double DK = K0 + (((EOS80_J * SR + polynome(T,EOS80_I)) * S + polynome(T,EOS80_H)) +
                          (polynome(T,EOS80_K) + polynome(T,EOS80_M) * S) * P) * P;

        final double K_35_0_P = polynome(P,EOS80_N);
        final double V_S_T_0  = SVAN_S_T_0 + V_35_0_0;
        return (SVAN_S_T_0*(1.0 - P/K_35_0_P) + V_S_T_0 * P * DK / (K_35_0_P * (K_35_0_P + DK)));
    }

    /**
     * Practical salinity scale 1978 definition
     * with temperature correction, XR = SQRT( Rt )
     */
    private static double sal(double RT, double XT) {
        return polynome(RT,PSS78_A) + (XT/(1.0+PSS78_K*XT)) * polynome(RT,PSS78_B);
    }

    /**
     * {@code dsal(RT,XT)} function for derivative
     * of {@code sal(RT,XT)} with <var>RT</var>.
     */
    private static double dsal(double RT, double XT) {
        return polynome(RT,PSS78_G) + (XT/(1.0+PSS78_K*XT)) * polynome(RT,PSS78_H);
    }

    /**
     * Computes salinity as a function of conductivity, temperature and pressure.
     *
     * @param C Conductivity in mS/cm (millisiemens by centimeters). Multiply
     *          par {@link #STANDARD_CONDUCTIVITY} if {@code C} is not a
     *          real conductivity, but instead the ratio between the sample's
     *          conductivity and the standard sample's conductivity.
     * @param T Temperature ITS-68 (-2 to 40°C)
     * @param P Pressure in decibars (0 to 10<sup>5</sup> dbar), not including atmospheric pressure.
     * @return  Salinity PSS-78.
     *
     * @todo What to do with pression!?! Check the equation of state.
     */
    public static double salinity(double C, final double T, final double P) {
        C /= STANDARD_CONDUCTIVITY;
        if (!(C < 5E-4)) { // use '!' in order to accept NaN
            final double XR = Math.sqrt(C/(polynome(T,PSS78_C) * (1.0 + polynome(P,PSS78_E) * P / 
                              ((PSS78_D[1] * T+PSS78_D[0]) * T + 1.0 + (PSS78_D[3] * T + PSS78_D[2]) * C))));
            final double S  = sal(XR, T-15.0);  // Do not use an 'assert' statement invoking 'cond'.
            if (!(S>=42)) return S; // use '!' to accept NaN
            /*
             * Calcule la salinité pour une eau de conductivité,
             * de température et de pression données. Cet algorithme
             * doit être utilisé lorsque l'on s'attend à une salinité
             * entre 42 et 50.
             */
            return 35 * C + C * (C-1) * (polynome(C,PSS78_AR) + T * (polynome(T,PSS78_AT) + C *
                        (PSS78_CR[0] + PSS78_CR[1] * C + PSS78_CR[2] * T)));
            // TODO: VERIFIER CE QUE DEVIENT LA PRESSION ET IMPLEMENTER L'EQUATION D'ETAT.
        } else {
            return 0; // Zero conductivity trap
        }
    }

    /**
     * Computes conductivity as a function of salinity, temperature and pressure.
     *
     * @param S Salinity PSS-78 (0 to 42)
     * @param T Temperature ITS-68 (-2 to 40°C)
     * @param P Pressure (0 to 10<sup>5</sup> dbar), not including atmospheric pressure.
     * @return  Conductivity in mS/cm.
     */
    public static double conductivity(final double S, final double T, final double P) {
        if (!(S < 0.02)) { // use '!' in order to accept NaN
            double XT = T-15.0;
            double RT = Math.sqrt(S/35.0);  // First approximation
            double SI = sal(RT,XT);
            for (int n=0; n<10; n++) {    // Iteration loop begin here with a maximum of 10 cycles
                RT += (S-SI)/dsal(RT,XT);
                SI = sal(RT,XT);
                if (Math.abs(SI-S) < 1E-4) break;
            }
            double RTT = polynome(T,PSS78_C)*(RT*RT);
            double AT  = PSS78_D[3]*T + PSS78_D[2];
            double BT  = (PSS78_D[1]*T + PSS78_D[0])*T + 1.0;
            double CP  = RTT*(BT + polynome(P,PSS78_E)*P);
            BT -= RTT*AT;
            // Solve quadratic equation for C = RT35*RT*(1+C/AR+b)
            double cnd = 0.5*(Math.sqrt(Math.abs((BT*BT) + 4.0*AT*CP)) - BT)/AT;
            return cnd*STANDARD_CONDUCTIVITY;
        } else {
            return 0; // Zero salinity trap
        }
    }

    /**
     * Computes specific heat as a function of salinity, temperature and pressure.
     *
     * @param S Salinity PSS-78.
     * @param T Temperature (°C).
     * @param P Pressure (dbar), not including atmospheric pressure.
     * @return  Specific heat (J/(kg&times;°C)).
     */
    public static double specificHeat(final double S, final double T, double P) {
        P /= 10.0;
        final double SR = Math.sqrt(S);
        return (polynome(T,HEAT_CC) + (polynome(T,HEAT_BB)*SR + polynome(T,HEAT_AA))*S +
            (((polynome(T,HEAT_C)*P + polynome(T,HEAT_B) )*P + polynome(T,HEAT_A) )*P) +
            ((((HEAT_J*SR+polynome(T,HEAT_H))*S*P + (HEAT_G*SR+polynome(T,HEAT_F))*S)*P +
                          (polynome(T,HEAT_E)*SR+polynome(T,HEAT_D))*S )*P));
    }

    /**
     * Computes fusion temperature (melting point) as a function of salinity and pressure.
     *
     * @param S Salinity PSS-78.
     * @param P Pressure (dbar), not including atmospheric pressure.
     * @return  Melting point (°C).
     */
    public static double fusionTemperature(final double S, final double P) {
        return (-0.0575 + 1.710523E-3*Math.sqrt(S) + -2.154996E-4*S)*S + -7.53E-4*P;
    }

    /**
     * Computes adiabetic temperature gradient as a function of salinity, temperature and pressure.
     *
     * @param S Salinity PSS-78.
     * @param T Temperature (°C).
     * @param P Pressure (dbar), not including atmospheric pressure.
     * @return  Adiabetic temperature gradient (°C/dbar).
     */
    public static double adiabeticTemperatureGradient(double S, final double T, final double P) {
        S -= 35.0;
        return (polynome(T,GRAD_A) + polynome(T,GRAD_B)*S +
               (polynome(T,GRAD_C) + polynome(T,GRAD_D)*S + polynome(T,GRAD_E)*P)*P);
    }

    /**
     * Computes depth as a function of pressure and latitude.
     *
     * @param  P Pressure (dbar), not including atmospheric pressure.
     * @param  lat Latitude in degrees (-90 to 90°)
     * @return Depth (m).
     */
    public static double depth(final double P, double lat) {
        lat = Math.sin(lat);
        lat *= lat;
        lat = 9.780318*( 1.0 + 5.2788E-3*lat + 2.36E-5*(lat*lat));
        return polynome(P,DEPTH_C)*P / (lat+(0.5*2.184E-6)*P);
    }

    /**
     * Computes sound velocity as a function of salinity, temperature and pressure.
     *
     * @param S Salinity PSS-78.
     * @param T Temperature (°C).
     * @param P Pressure (dbar), not including atmospheric pressure.
     * @return  Sound velocity (m/s).
     */
    public static double soundVelocity(final double S, final double T, final double P) {
        // S^0 terms
        final double CW = ((polynome(T,SOUND_C3) *P + polynome(T,SOUND_C2))*P +
                            polynome(T,SOUND_C1))*P + polynome(T,SOUND_C0);
        // S^1 terms
        final double A  = ((polynome(T,SOUND_A3) *P + polynome(T,SOUND_A2))*P +
                            polynome(T,SOUND_A1))*P + polynome(T,SOUND_A0);
        // S^3/2 terms
        final double B  = polynome(T,SOUND_B0) + polynome(T,SOUND_B1)*P;

        // S^2 terms
        final double D  = SOUND_D0 + SOUND_D1*P;

        // sound speed return
        return CW + (D*S + B*Math.sqrt(S) + A)*S;
    }

    /**
     * Computes saturation in disolved oxygen as a function of salinity and temperature.
     *
     * @param S Salinity PSS-78.
     * @param T Temperature (°C).
     * @return  Saturation in disolved oxygen (µmol/kg).
     */
    public static double saturationO2(final double S, double T) {
        T += 273.15;
        return Math.exp(polynome_neg(T,O2_AT) + S*polynome_neg(T,O2_AS));
    }

    /**
     * Calcule la valeur d'un polynôme.
     * Cette fonction calcule la valeur de:
     *
     * <blockquote><pre>
     *    y = C[0] + C[1]*x + C[2]*x² + C[3]*x³
     * </pre></blockquote>
     *
     * où C est un vecteur de coéfficients transmis en argument.
     * Une exception sera levée si ce tableau ne contient pas
     * au moins 1 élément.
     *
     * @param x Valeur x à laquelle calculer le polynôme.
     * @param c Coéfficients C du polynôme.
     * @return  La valeur du polynôme au x spécifié.
     *
     * @see #poly_inv(double,double[])
     */
    private static double polynome(final double x, final double c[]) {
        int n = c.length-1;
        double y = c[n];
        while (n > 0) {
            y = y*x + c[--n];
        }
        return y;
    }

    /**
     * Calcule la valeur de:
     *
     * <blockquote><pre>
     *    y = C[0] + C[1]/x + C[2]/x² + C[3]/x³
     * </pre></blockquote>
     *
     * où C est un vecteur de coéfficients transmis en argument.
     * Une exception sera levée si ce tableau ne contient pas
     * au moins 1 élément.
     *
     * @param x Valeur x à laquelle calculer le polynôme.
     * @param C Coéfficients C du polynôme.
     * @return  La valeur du polynôme au x spécifié.
     *
     * @see #polynome(double,double[])
     */
    private static double polynome_neg(final double x, final double c[]) {
        int n = c.length-1;
        double y = c[n];
        while (n > 0) {
            y = y/x + c[--n];
        }
        return y;
    }
}
